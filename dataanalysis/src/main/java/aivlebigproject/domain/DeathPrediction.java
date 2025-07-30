package aivlebigproject.domain;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DeathPrediction_table")
@Data
@IdClass(DeathPredictionId.class)
public class DeathPrediction {

    @Id
    private LocalDate date;
    @Id
    private String region;

    private Long Deaths; // <-- 필드 이름을 "Deaths"로 통일

    public DeathPrediction() {}

    public DeathPrediction(LocalDate date, String region, Long Deaths) { // <-- 생성자 인자 변경
        this.date = date;
        this.region = region;
        this.Deaths = Deaths; // <-- Deaths 필드 초기화
    }

}
