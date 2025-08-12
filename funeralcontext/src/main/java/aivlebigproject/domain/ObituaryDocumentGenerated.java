package aivlebigproject.domain;

import java.util.Date;
import lombok.Data;

// Python에서 보낸 '부고장 문서 생성 완료' 이벤트를 담기 위한 DTO 클래스
@Data
public class ObituaryDocumentGenerated {

    private String eventType; // JSON 파싱 오류 방지를 위해 추가
    private Long obituaryId;
    private Long funeralInfoId;
    private Long obituaryTemplateId; // 향후 기능 확장을 위해 추가
    private String obituaryFileName;
    private String obituaryFileUrl;
    private String obituaryStatus;
    private Date obituaryCreatedAt;
    private String funeralHomeAddressUrl; // 지도 URL
}