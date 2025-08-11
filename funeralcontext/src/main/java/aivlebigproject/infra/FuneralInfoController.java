// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/FuneralInfoController.java
// 역할 : FuneralInfo와 관련된 문서 생성 요청을 처리
// ========================================

package aivlebigproject.infra;

import aivlebigproject.domain.*;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils; // [추가] BeanUtils import

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/funeralInfos")
@Transactional
public class FuneralInfoController {

    @Autowired
    FuneralInfoRepository funeralInfoRepository;
    
    @Autowired
    ObituaryRepository obituaryRepository;

    // [추가] DeathReportRepository와 ScheduleRepository를 주입받습니다.
    @Autowired
    DeathReportRepository deathReportRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    // [수정] updateFuneralInfo 메서드를 @RequestBody를 사용하도록 변경합니다.
    @RequestMapping(
        value = "/funeralInfos/{id}/updatefuneralinfo",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public FuneralInfo updateFuneralInfo(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateFuneralInfoCommand command
    ) throws Exception {
        System.out.println(
            "##### /funeralInfo/updateFuneralInfo  called #####"
        );
        Optional<FuneralInfo> optionalFuneralInfo = funeralInfoRepository.findById(
            id
        );

        optionalFuneralInfo.orElseThrow(() -> new Exception("No Entity Found"));
        FuneralInfo funeralInfo = optionalFuneralInfo.get();
        funeralInfo.updateFuneralInfo(command);

        funeralInfoRepository.save(funeralInfo);
        return funeralInfo;
    }
    
    @RequestMapping(
        value = "/funeralInfos/{id}/createobituary",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    // [수정] 반환 타입을 FuneralInfo에서 Obituary로 변경합니다.
    public Obituary createObituary(@PathVariable(value = "id") Long id) throws Exception {
        System.out.println("##### /funeralInfo/createObituary called #####");
        
        Optional<FuneralInfo> optionalFuneralInfo = funeralInfoRepository.findById(id);
        optionalFuneralInfo.orElseThrow(() -> new Exception("No Entity Found"));
        FuneralInfo funeralInfo = optionalFuneralInfo.get();

        // --- [수정] 핵심 로직 변경 ---
        // 1. 새로운 Obituary 객체를 생성합니다.
        Obituary obituary = new Obituary();

        // 2. FuneralInfo의 데이터를 Obituary 객체로 복사합니다.
        //    (BeanUtils를 사용하면 필드 이름이 같은 값을 자동으로 복사해줍니다.)
        BeanUtils.copyProperties(funeralInfo, obituary);
        
        // 3. Obituary의 고유 상태를 설정합니다.
        obituary.setFuneralInfoId(funeralInfo.getFuneralInfoId()); // FK 명시적 설정
        obituary.setObituaryStatus("PENDING");

        // [추가] 생성 시점을 기록합니다.
        obituary.setObituaryCreatedAt(new Date()); 
        
        // 4. DB에 저장하여 고유 ID(obituaryId)를 부여받습니다.
        obituaryRepository.save(obituary);

        // 5. Python 서비스에 작업을 요청하기 위해 이벤트를 발행합니다.
        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(obituary);
        obituaryDataCreated.publishAfterCommit();

        // 6. ID가 부여된 Obituary 객체를 FE에 즉시 반환합니다.
        return obituary;
    }

    // [수정] createSchedule 메서드를 부고장과 동일한 방식으로 수정합니다.
    @RequestMapping(
        value = "/funeralInfos/{id}/createschedule",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Schedule createSchedule(@PathVariable(value = "id") Long id) throws Exception {
        System.out.println("##### /funeralInfo/createSchedule  called #####");
        
        FuneralInfo funeralInfo = funeralInfoRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));

        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(funeralInfo, schedule);
        schedule.setFuneralInfoId(funeralInfo.getFuneralInfoId());
        schedule.setScheduleStatus("PENDING");
        schedule.setScheduleCreatedAt(new Date());
        scheduleRepository.save(schedule);

        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(funeralInfo, schedule);
        scheduleDataCreated.publishAfterCommit();

        return schedule;
    }

    // [수정] createDeathReport 메서드를 부고장과 동일한 방식으로 수정합니다.
    @RequestMapping(
        value = "/funeralInfos/{id}/createdeathreport",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public DeathReport createDeathReport(@PathVariable(value = "id") Long id) throws Exception {
        System.out.println(
            "##### /funeralInfo/createDeathReport  called #####"
        );
        
        FuneralInfo funeralInfo = funeralInfoRepository.findById(id)
            .orElseThrow(() -> new Exception("No Entity Found"));

        DeathReport deathReport = new DeathReport();
        BeanUtils.copyProperties(funeralInfo, deathReport);
        deathReport.setFuneralInfoId(funeralInfo.getFuneralInfoId());
        deathReport.setDeathReportStatus("PENDING");
        deathReport.setDeathReportCreatedAt(new Date());
        deathReportRepository.save(deathReport);

        DeathReportDataCreated deathReportDataCreated = new DeathReportDataCreated(funeralInfo, deathReport);
        deathReportDataCreated.publishAfterCommit();

        return deathReport;
    }
}
//>>> Clean Arch / Inbound Adaptor
