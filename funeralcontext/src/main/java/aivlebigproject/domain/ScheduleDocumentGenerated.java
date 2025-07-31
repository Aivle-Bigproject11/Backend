package aivlebigproject.domain;

import java.util.Date;
import lombok.Data;

// Python에서 보낸 '장례일정표 문서 생성 완료' 이벤트를 담기 위한 DTO 클래스
@Data
public class ScheduleDocumentGenerated {

    private String eventType;
    private Long scheduleId;
    private Long funeralInfoId;
    private Long scheduleTemplateId;
    private String scheduleFilePath;
    private String scheduleFileUrl;
    private String scheduleStatus;
    private Date scheduleCreatedAt;
}