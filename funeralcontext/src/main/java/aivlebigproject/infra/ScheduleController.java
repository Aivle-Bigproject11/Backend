// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/infra/ScheduleController.java
// 역할 : 장례일정표(Schedule) 생성 요청을 처리하는 API 컨트롤러
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
// @RequestMapping(value="/schedules")
@Transactional
public class ScheduleController {

    @Autowired
    ScheduleRepository scheduleRepository;

    // [주석] FuneralInfo 데이터를 조회하기 위해 FuneralInfoRepository를 주입받습니다.
    @Autowired
    FuneralInfoRepository funeralInfoRepository;

    // [주석] 장례일정표 생성 요청을 처리하는 API 엔드포인트입니다.
    @PutMapping("/funeralInfos/{id}/createschedule")
    public Schedule createSchedule(@PathVariable(value = "id") Long funeralInfoId) throws Exception {
        
        // 1. 전달받은 funeralInfoId로 원본 장례 정보를 찾아옵니다.
        FuneralInfo funeralInfo = funeralInfoRepository.findById(funeralInfoId)
            .orElseThrow(() -> new Exception("FuneralInfo not found"));

        // 2. 새로운 Schedule 객체를 생성합니다.
        Schedule schedule = new Schedule();

        // 3. FuneralInfo의 데이터를 Schedule 객체로 복사합니다.
        BeanUtils.copyProperties(funeralInfo, schedule);
        
        // 4. Schedule의 고유 상태를 설정합니다.
        schedule.setFuneralInfoId(funeralInfo.getFuneralInfoId());
        schedule.setScheduleStatus("PENDING");
        schedule.setScheduleCreatedAt(new Date());

        // 5. DB에 저장하여 고유 ID(scheduleId)를 부여받습니다.
        scheduleRepository.save(schedule);

        // 6. Python 서비스에 작업을 요청하기 위해 이벤트를 발행합니다.
        ScheduleDataCreated scheduleDataCreated = new ScheduleDataCreated(funeralInfo, schedule);
        scheduleDataCreated.publishAfterCommit();

        // 7. ID가 부여된 Schedule 객체를 FE에 즉시 반환합니다.
        return schedule;
    }
}
//>>> Clean Arch / Inbound Adaptor
