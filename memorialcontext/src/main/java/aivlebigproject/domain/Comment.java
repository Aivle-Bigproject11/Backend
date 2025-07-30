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
@Table(name = "Comment_table")
@Data
//<<< DDD / Aggregate Root
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    private String memorialId;

    private String name;

    private String relationship;

    private String content;

    private Date createdAt;

    public static CommentRepository repository() {
        CommentRepository commentRepository = MemorialcontextApplication.applicationContext.getBean(
            CommentRepository.class
        );
        return commentRepository;
    }
}
//>>> DDD / Aggregate Root
