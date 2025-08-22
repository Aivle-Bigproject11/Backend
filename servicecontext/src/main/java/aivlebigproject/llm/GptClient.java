package aivlebigproject.llm;

import aivlebigproject.domain.dto.ParsedResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GptClient {

    // [수정] application.yml을 통해 주입받을 API 키를 저장할 멤버 변수입니다.
    // 기존의 static final String API_KEY 라인은 삭제했습니다.
    @Value("${openai.api.key}")
    private String openaiApiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    // [수정] Spring Component에서는 static이 아닌 멤버 변수로 선언하는 것이 일반적입니다.
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String callChatGpt(List<Map<String, String>> messages) throws Exception {
        String requestBody = mapper.writeValueAsString(Map.of(
                "model", "gpt-4.1-mini", // 모델명을 gpt-4.1-mini에서 최신 모델로 변경했습니다.
                "messages", messages,
                "temperature", 0.5
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                // [핵심] 하드코딩된 API_KEY 대신, @Value로 주입받은 this.openaiApiKey를 사용합니다.
                .header("Authorization", "Bearer " + this.openaiApiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("GPT 응답 원문:\n{}", response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GPT 호출 실패: " + response.body());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("choices").get(0).get("message").get("content").asText();
    }

    public ParsedResult parse(String gptResponse) {
        log.info("📦 GPT 파싱 시작 - 원문:\n{}", gptResponse);

        String[] lines = gptResponse.split("\n");
        String service1Name = "";
        String service2Name = "";
        StringBuilder message = new StringBuilder();
        boolean inServiceSection = false;
        boolean inMessageSection = false;

        for (String line : lines) {
            line = line.trim();

            if (line.contains("[추천된 전환서비스]")) {
                inServiceSection = true;
                inMessageSection = false;
                continue;
            }
            if (line.contains("[메시지 내용]")) {
                inServiceSection = false;
                inMessageSection = true;
                continue;
            }

            if (inServiceSection && line.startsWith("-")) {
                if (service1Name.isEmpty()) {
                    service1Name = line.substring(1).trim();
                } else if (service2Name.isEmpty()) {
                    service2Name = line.substring(1).trim();
                }
            } else if (inMessageSection) {
                message.append(line).append(" ");
            }
        }

        ParsedResult result = new ParsedResult();
        result.setService1(service1Name);
        result.setService2(service2Name);
        result.setMessage(message.toString().trim());

        return result;
    }
}