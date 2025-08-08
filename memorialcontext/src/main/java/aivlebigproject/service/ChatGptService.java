package aivlebigproject.service;

import aivlebigproject.dto.ChatGPTRequest;
import aivlebigproject.dto.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    public String generateMessage(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }
}
