package aivlebigproject.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatGPTMessage {
    String role;
    String content;
}
