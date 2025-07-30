package aivlebigproject.infra.controller;

import aivlebigproject.domain.FuneralInfoRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaTestController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/funeral-regist")
    public String sendEvent() {

        FuneralInfoRegistered event = new FuneralInfoRegistered();
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