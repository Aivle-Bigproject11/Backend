package aivlebigproject.domain;

import java.util.Date;
import lombok.Data;

// Python에서 보낸 '사망신고서 문서 생성 완료' 이벤트를 담기 위한 DTO 클래스
@Data
public class DeathReportDocumentGenerated {

    private String eventType;
    private Long deathReportId;
    private Long funeralInfoId;
    private Long deathReportTemplateId;
    private String deathReportFilePath;
    private String deathReportFileUrl;
    private String deathReportStatus;
    private Date deathReportCreatedAt;
}