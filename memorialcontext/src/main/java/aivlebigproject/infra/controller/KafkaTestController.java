package aivlebigprojectminseo.infra.controller;

import aivlebigprojectminseo.domain.ResistedFuneralInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaTestController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/resistedFuneralInfo")
    public String sendEvent() {

        ResistedFuneralInfo event = new ResistedFuneralInfo();
        event.setCustomerId(1L);
        event.setDeceasedName("고민서");
        event.setDeceasedAge(75);
        event.setDeceasedBirthOfDate(LocalDate.of(1949, 1, 1));
        event.setDeceasedDate(LocalDate.of(2024, 7, 25));
        event.setDeceasedGender("남");

        event.publish();
        return "Event Sent!";
    }
}