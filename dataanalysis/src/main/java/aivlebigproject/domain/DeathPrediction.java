package aivlebigproject.domain;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DeathPrediction_table")
@Data
public class DeathPrediction {

    @EmbeddedId
    private DeathPredictionId id;

    private Long deaths;

    public DeathPrediction() {}

    public DeathPrediction(String date, String region, Long deaths) {
        this.id = new DeathPredictionId(date, region);
        this.deaths = deaths;
    }

    public String getDate() {
        return id != null ? id.getDate() : null;
    }

    public String getRegion() {
        return id != null ? id.getRegion() : null;
    }
}
