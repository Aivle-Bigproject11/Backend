package aivlebigproject.controller;

import aivlebigproject.event.listener.FuneralInfoRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaTestController {

    @GetMapping("/funeral-regist")
    public String sendEvent() {
        FuneralInfoRegistered event1 = new FuneralInfoRegistered();
        event1.setFuneralInfoId(1L);
        event1.setCustomerId(1001L);
        event1.setDeceasedName("김철수");
        event1.setDeceasedAge(75);
        event1.setDeceasedBirthOfDate(toDate(LocalDate.of(1949, 1, 1)));
        event1.setDeceasedDate(toDate(LocalDate.of(2024, 7, 25)));
        event1.setDeceasedGender("남성");
        event1.publish();

        FuneralInfoRegistered event2 = new FuneralInfoRegistered();
        event2.setFuneralInfoId(2L);
        event2.setCustomerId(1002L);
        event2.setDeceasedName("이영희");
        event2.setDeceasedAge(82);
        event2.setDeceasedBirthOfDate(toDate(LocalDate.of(1942, 3, 15)));
        event2.setDeceasedDate(toDate(LocalDate.of(2024, 6, 30)));
        event2.setDeceasedGender("여성");
        event2.publish();

        FuneralInfoRegistered event3 = new FuneralInfoRegistered();
        event3.setFuneralInfoId(3L);
        event3.setCustomerId(1003L);
        event3.setDeceasedName("박철수");
        event3.setDeceasedAge(68);
        event3.setDeceasedBirthOfDate(toDate(LocalDate.of(1956, 9, 20)));
        event3.setDeceasedDate(toDate(LocalDate.of(2024, 8, 1)));
        event3.setDeceasedGender("남성");
        event3.publish();

        FuneralInfoRegistered event4 = new FuneralInfoRegistered();
        event4.setFuneralInfoId(4L);
        event4.setCustomerId(1004L);
        event4.setDeceasedName("최지은");
        event4.setDeceasedAge(90);
        event4.setDeceasedBirthOfDate(toDate(LocalDate.of(1934, 11, 2)));
        event4.setDeceasedDate(toDate(LocalDate.of(2024, 7, 10)));
        event4.setDeceasedGender("여성");
        event4.publish();

        FuneralInfoRegistered event5 = new FuneralInfoRegistered();
        event5.setFuneralInfoId(5L);
        event5.setCustomerId(1005L);
        event5.setDeceasedName("정수현");
        event5.setDeceasedAge(60);
        event5.setDeceasedBirthOfDate(toDate(LocalDate.of(1964, 5, 27)));
        event5.setDeceasedDate(toDate(LocalDate.of(2024, 6, 15)));
        event5.setDeceasedGender("남성");
        event5.publish();
        return "FuneralInfoRegistered Event Sent!";
    }

    private Date toDate(LocalDate localDate) {
        return localDate == null ? null
                : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}