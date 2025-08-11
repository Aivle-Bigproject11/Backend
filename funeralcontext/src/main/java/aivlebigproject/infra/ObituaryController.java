package aivlebigproject.infra;

import aivlebigproject.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date; // [추가]
import org.springframework.beans.BeanUtils; // [추가]
//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/obituaries")
@Transactional
public class ObituaryController {

    @Autowired
    ObituaryRepository obituaryRepository;

    // [추가] FuneralInfo 데이터를 조회하기 위해 FuneralInfoRepository를 주입받습니다.
    @Autowired
    FuneralInfoRepository funeralInfoRepository;

    // [추가] 부고장 생성 요청을 처리하는 새로운 API 엔드포인트입니다.
    @PutMapping("/funeralInfos/{id}/createobituary")
    public Obituary createObituary(@PathVariable(value = "id") Long funeralInfoId) throws Exception {
        
        // 1. 전달받은 funeralInfoId로 원본 장례 정보를 찾아옵니다.
        FuneralInfo funeralInfo = funeralInfoRepository.findById(funeralInfoId)
            .orElseThrow(() -> new Exception("FuneralInfo not found"));

        // 2. 새로운 Obituary 객체를 생성합니다.
        Obituary obituary = new Obituary();

        // 3. FuneralInfo의 데이터를 Obituary 객체로 복사합니다.
        BeanUtils.copyProperties(funeralInfo, obituary);
        
        // 4. Obituary의 고유 상태를 설정합니다.
        obituary.setFuneralInfoId(funeralInfo.getFuneralInfoId());
        obituary.setObituaryStatus("PENDING");
        obituary.setObituaryCreatedAt(new Date());

        // 5. DB에 저장하여 고유 ID(obituaryId)를 부여받습니다.
        obituaryRepository.save(obituary);

        // 6. Python 서비스에 작업을 요청하기 위해 이벤트를 발행합니다.
        //    (새로운 생성자를 사용하여 FuneralInfo와 Obituary 정보를 모두 담습니다.)
        ObituaryDataCreated obituaryDataCreated = new ObituaryDataCreated(funeralInfo, obituary);
        obituaryDataCreated.publishAfterCommit();

        // 7. ID가 부여된 Obituary 객체를 FE에 즉시 반환합니다.
        return obituary;
    }
}
//>>> Clean Arch / Inbound Adaptor
