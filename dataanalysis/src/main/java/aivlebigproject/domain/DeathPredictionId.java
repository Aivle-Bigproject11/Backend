package aivlebigproject.domain;

import lombok.*;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DeathPredictionId implements Serializable {
    private String date;
    private String region;
}