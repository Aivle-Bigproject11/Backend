package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import aivlebigproject.domain.repository.PhotoRepository;

import java.util.Date;
import java.util.UUID;
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

    private UUID memorialId;

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
