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
    private static final String API_KEY = "sk";  // 🔐 실제 API 키 사용
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * GPT API 호출
     */
    public String callChatGpt(List<Map<String, String>> messages) throws Exception {
        String requestBody = mapper.writeValueAsString(Map.of(
                "model", "gpt-4o-nano",
                "messages", messages,
                "temperature", 0.7
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

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
        // 예상 응답 예시:
        // 첫째: 프리미엄 골프용품 서비스
        // 둘째: 수연 서비스
        // 메시지: 60대 여성 고객님께 추천하는 특별한 서비스입니다...

        String[] lines = gptResponse.split("\n");
        String service1 = "";
        String service2 = "";
        StringBuilder message = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("첫째:") || line.toLowerCase().startsWith("first")) {
                service1 = line.split(":", 2)[1].trim();
            } else if (line.startsWith("둘째:") || line.toLowerCase().startsWith("second")) {
                service2 = line.split(":", 2)[1].trim();
            } else {
                message.append(line).append(" ");
            }
        }

        if (service1.isEmpty() || service2.isEmpty()) {
            throw new IllegalArgumentException("GPT 응답에 추천 서비스명이 없습니다.\n" + gptResponse);
        }

        return new ParsedResult(service1, service2, message.toString().trim());
    }
}