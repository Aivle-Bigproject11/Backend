package aivlebigproject.infra;

import aivlebigproject.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    CustomerProfileRepository customerProfileRepository;

    @Autowired
    FamilyRepository familyRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 유가족 등록 이벤트를 처리하는 메서드
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverFamilyRegistered_Handle(@Payload FamilyRegistered familyRegistered) {
        if (familyRegistered.validate()) { // 이벤트가 유효한지 확인
            System.out.println(
                "##### FamilyRegistered 이벤트를 받았습니다: " + familyRegistered.toString()
            );
            
            // TODO: 여기에 FamilyRegistered 이벤트에 대한 비즈니스 로직을 구현합니다.
            // 예: 새로운 유가족 등록 알림을 관리자에게 보내거나,
            //     추모관 서비스에 추모관 생성 요청을 보낼 수 있습니다.
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverManagerRegistered_Handle(@Payload ManagerRegistered managerRegistered) {
        if (managerRegistered.validate()) { // 이벤트가 유효한지 확인
            System.out.println(
                "##### ManagerRegistered 이벤트를 받았습니다: " + managerRegistered.toString()
            );
            
            // TODO: 여기에 ManagerRegistered 이벤트에 대한 비즈니스 로직을 구현합니다.
            // 예: 신규 관리자에게 초기 권한을 부여하거나,
            //     환영 이메일을 보내는 등의 작업을 수행할 수 있습니다.
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
