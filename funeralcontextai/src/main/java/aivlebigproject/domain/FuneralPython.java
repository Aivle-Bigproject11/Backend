package aivlebigproject.domain;

import aivlebigproject.FuneralcontextaiApplication;
import aivlebigproject.domain.DeathReportDocumentGenerated;
import aivlebigproject.domain.ObituaryDocumentGenerated;
import aivlebigproject.domain.ScheduleDocumentGenerated;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "FuneralPython_table")
@Data
//<<< DDD / Aggregate Root
public class FuneralPython {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @PostPersist
    public void onPostPersist() {
        ObituaryDocumentGenerated obituaryDocumentGenerated = new ObituaryDocumentGenerated(
            this
        );
        obituaryDocumentGenerated.publishAfterCommit();

        DeathReportDocumentGenerated deathReportDocumentGenerated = new DeathReportDocumentGenerated(
            this
        );
        deathReportDocumentGenerated.publishAfterCommit();

        ScheduleDocumentGenerated scheduleDocumentGenerated = new ScheduleDocumentGenerated(
            this
        );
        scheduleDocumentGenerated.publishAfterCommit();
    }

    public static FuneralPythonRepository repository() {
        FuneralPythonRepository funeralPythonRepository = FuneralcontextaiApplication.applicationContext.getBean(
            FuneralPythonRepository.class
        );
        return funeralPythonRepository;
    }
}
//>>> DDD / Aggregate Root
