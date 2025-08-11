// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/infra/DeathReportController.java
// 역할 : 사망신고서(DeathReport) 생성 요청을 처리하는 API 컨트롤러
// ========================================

package aivlebigproject.infra;

import aivlebigproject.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import org.springframework.beans.BeanUtils;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/deathReports")
@Transactional
public class DeathReportController {

    @Autowired
    DeathReportRepository deathReportRepository;

    // [주석] FuneralInfo 데이터를 조회하기 위해 FuneralInfoRepository를 주입받습니다.
    @Autowired
    FuneralInfoRepository funeralInfoRepository;

    // [주석] 사망신고서 생성 요청을 처리하는 API 엔드포인트입니다.
    //       기존에는 FuneralInfoController에 있었지만, 역할 분리를 위해 이곳으로 이동했습니다.
    @PutMapping("/funeralInfos/{id}/createdeathreport")
    public DeathReport createDeathReport(@PathVariable(value = "id") Long funeralInfoId) throws Exception {
        
        // 1. 전달받은 funeralInfoId로 원본 장례 정보를 찾아옵니다.
        FuneralInfo funeralInfo = funeralInfoRepository.findById(funeralInfoId)
            .orElseThrow(() -> new Exception("FuneralInfo not found"));

        // 2. 새로운 DeathReport 객체를 생성합니다.
        DeathReport deathReport = new DeathReport();

        // 3. FuneralInfo의 데이터를 DeathReport 객체로 복사합니다.
        BeanUtils.copyProperties(funeralInfo, deathReport);
        
        // 4. DeathReport의 고유 상태를 설정합니다.
        deathReport.setFuneralInfoId(funeralInfo.getFuneralInfoId());
        deathReport.setDeathReportStatus("PENDING");
        deathReport.setDeathReportCreatedAt(new Date());

        // 5. DB에 저장하여 고유 ID(deathReportId)를 부여받습니다.
        deathReportRepository.save(deathReport);

        // 6. Python 서비스에 작업을 요청하기 위해 이벤트를 발행합니다.
        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(funeralInfo, deathReport);
        deathReportDataCreated.publishAfterCommit();

        // 7. ID가 부여된 DeathReport 객체를 FE에 즉시 반환합니다.
        return deathReport;
    }
}
//>>> Clean Arch / Inbound Adaptor
