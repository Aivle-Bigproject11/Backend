package aivlebigprojectminseo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "video_images")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VideoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @JsonIgnore // 순환 참조 방지
    private Video video;

    @Column(name = "image_id", nullable = false, unique = true)
    private String imageId;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "blob_name", nullable = false)
    private String blobName;

    @Column(name = "blob_url", nullable = false)
    private String blobUrl;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "upload_order")
    private Integer uploadOrder;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}