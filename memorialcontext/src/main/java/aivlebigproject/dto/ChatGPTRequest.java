package aivlebigproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatGPTRequest {
    private String model;
    private List<ChatGPTMessage> messages;

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new ChatGPTMessage("user", prompt));
    }
}
