package aivlebigproject.domain;

import aivlebigproject.ServicecontextApplication;
import aivlebigproject.domain.GeneratedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RecommendMessage_table")
@Data
//<<< DDD / Aggregate Root
public class RecommendMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    private String comment;

    private Long serviceId1;

    private Long serviceId2;

    private Long customerId;

    private Date createMessageDate;

    @PostPersist
    public void onPostPersist() {
        GeneratedMessage generatedMessage = new GeneratedMessage(this);
        generatedMessage.publishAfterCommit();
    }

    public static RecommendMessageRepository repository() {
        RecommendMessageRepository recommendMessageRepository = ServicecontextApplication.applicationContext.getBean(
            RecommendMessageRepository.class
        );
        return recommendMessageRepository;
    }
}
//>>> DDD / Aggregate Root
