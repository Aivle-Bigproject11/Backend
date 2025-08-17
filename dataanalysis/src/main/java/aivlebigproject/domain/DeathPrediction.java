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
    private Double growthRate; // 📌 Double로 변경
    private Double regionalPercentage; // 📌 Double로 변경
    private Long previousYearDeaths;

    public DeathPrediction() {}

    public DeathPrediction(String date, String region, Long deaths,Double growthRate,Double regionalPercentage, Long previousYearDeaths) {
        this.id = new DeathPredictionId(date, region);
        this.deaths = deaths;
        this.growthRate = growthRate;
        this.regionalPercentage=regionalPercentage;
        this.previousYearDeaths=previousYearDeaths;
    }

    public String getDate() {
        return id != null ? id.getDate() : null;
    }

    public String getRegion() {
        return id != null ? id.getRegion() : null;
    }
}
