package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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
            customerInfo.setAge(customerRegistered.getAge());
            customerInfo.setGender(customerRegistered.getGender());
            customerInfo.setHasChildren(customerRegistered.getHasChildren());
            customerInfo.setIsMarried(customerRegistered.getIsMarried());

            // ✅ List<String> 그대로 전달 (컨버터가 자동 처리)
            customerInfo.setDisease(customerRegistered.getDiseaseList());

            System.out.println("🧪 저장할 데이터: " + customerInfo.toString());
            customerInfoRepository.save(customerInfo);
            System.out.println("✅ 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}