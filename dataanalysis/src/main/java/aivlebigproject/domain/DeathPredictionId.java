package aivlebigproject.domain;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeathPredictionId implements Serializable {

    private YearMonth date;
    private String region;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeathPredictionId that = (DeathPredictionId) o;
        return Objects.equals(date, that.date) && Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, region);
    }
}