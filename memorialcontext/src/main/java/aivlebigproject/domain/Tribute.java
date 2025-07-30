package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Tribute_table")
@Data
//<<< DDD / Aggregate Root
public class Tribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tributeId;

    private String memorialId;

    private String content;

    private String keywords;

    private Date createdAt;

    private Date updatedAt;

    public static TributeRepository repository() {
        TributeRepository tributeRepository = MemorialcontextApplication.applicationContext.getBean(
            TributeRepository.class
        );
        return tributeRepository;
    }
}
//>>> DDD / Aggregate Root
