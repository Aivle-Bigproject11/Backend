package aivlebigproject.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GptClient {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = " ";  // 🔐 실제 API 키 사용
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**http POST :8084/customerProfiles \
     name="홍길동" \
     age:=75 \
     phone="01012345678" \
     job="교사" \
     address="서울시 강남구" \
     gender="여성" \
     birthOfDate="1970-01-01T00:00:00.000+0000" \
     hasChildren:=true \
     isMarried:=true \
     diseaseList:='["고혈압", "당뇨"]' \
     rrn="700101-1234567"
     * GPT API 호출
     */
    public String callChatGpt(List<Map<String, String>> messages) throws Exception {
        String requestBody = mapper.writeValueAsString(Map.of(
                "model", "gpt-4.1-mini",
                "messages", messages,
                "temperature", 0.5
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("GPT 응답 원문:\n{}", response.body());

        if (response.statusCode() != 200) {
            log.error("GPT API 호출 실패: {}", response.body());
            throw new RuntimeException("GPT 호출 실패: " + response.body());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("choices").get(0).get("message").get("content").asText();
    }

    /**
     * GPT 응답 파싱
     */
    public static ParsedResult parse(String gptResponse) {
        String[] lines = gptResponse.split("\n");
        String service1 = "";
        String service2 = "";
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
                if (service1.isEmpty()) {
                    service1 = line.substring(1).trim();
                } else if (service2.isEmpty()) {
                    service2 = line.substring(1).trim();
                }
            } else if (inMessageSection) {
                message.append(line).append(" ");
            }
        }

        if (service1.isEmpty() || service2.isEmpty()) {
            throw new IllegalArgumentException("GPT 응답에 추천 서비스명이 없습니다.\n" + gptResponse);
        }

        return new ParsedResult(service1, service2, message.toString().trim());

    }
}