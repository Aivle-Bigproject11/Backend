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
@Table(name = "Photo_table")
@Data
//<<< DDD / Aggregate Root
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long photoId;

    private String memorialId;

    private String title;

    private String imageUrl;

    private Date uploadedAt;

    public static PhotoRepository repository() {
        PhotoRepository photoRepository = MemorialcontextApplication.applicationContext.getBean(
            PhotoRepository.class
        );
        return photoRepository;
    }
}
//>>> DDD / Aggregate Root
