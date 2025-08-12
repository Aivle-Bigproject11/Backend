package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerInfoViewHandler {

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerRegistered_then_CREATE_1(
            @Payload CustomerRegistered customerRegistered
    ) {
        try {
            if (!customerRegistered.validate()) {
                System.out.println("❌ CustomerRegistered 이벤트 유효성 검사 실패: " + customerRegistered);
                return;
            }

            System.out.println("📩 [ServiceContext] 이벤트 수신: userId = " + customerRegistered.getUserId());

            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setId(customerRegistered.getCustomerId());
            customerInfo.setName(customerRegistered.getName());
            customerInfo.setAge(customerRegistered.getAge());
            customerInfo.setPhone(customerRegistered.getPhone());
            customerInfo.setJob(customerRegistered.getJob());
            customerInfo.setAddress(customerRegistered.getAddress());
            customerInfo.setGender(customerRegistered.getGender());
            customerInfo.setHasChildren(customerRegistered.getHasChildren());
            customerInfo.setIsMarried(customerRegistered.getIsMarried());
            customerInfo.setBirthDate(customerRegistered.getBirthDate());
            customerInfo.setDisease(customerRegistered.getDiseaseList());

            // ✅ List<String> 그대로 전달 (컨버터가 자동 처리)
            customerInfo.setDisease(customerRegistered.getDiseaseList());

            System.out.println("🧪 저장할 데이터: " + customerInfo.toString());
            customerInfoRepository.save(customerInfo);
            System.out.println("✅ 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerUpdated_then_UPDATE_1(
            @Payload CustomerUpdated customerUpdated
    ) {
        try {
            if (!customerUpdated.validate()) return;
            // view 객체 조회
            Optional<CustomerInfo> customerInfoOptional = customerInfoRepository.findById(
                    customerUpdated.getCustomerId()
            );

            if (customerInfoOptional.isPresent()) {
                CustomerInfo customerInfo = customerInfoOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                customerInfo.setAge(customerUpdated.getAge());
                customerInfo.setName(customerUpdated.getName());
                customerInfo.setPhone(customerUpdated.getPhone());
                customerInfo.setJob(customerUpdated.getJob());
                customerInfo.setAddress(customerUpdated.getAddress());
                customerInfo.setGender(customerUpdated.getGender());
                customerInfo.setBirthDate(customerUpdated.getBirthDate());
                customerInfo.setHasChildren(customerUpdated.getHasChildren());
                customerInfo.setIsMarried(customerUpdated.getIsMarried());
                // view 레파지 토리에 save
                customerInfoRepository.save(customerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerDeleted_then_DELETE_1(
            @Payload CustomerDeleted customerDeleted
    ) {
        try {
            if (!customerDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            customerInfoRepository.deleteById(customerDeleted.getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}