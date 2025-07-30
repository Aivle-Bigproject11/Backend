package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class CustomerReadmodelViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CustomerReadmodelRepository customerReadmodelRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerRegistered_then_CREATE_1(
        @Payload CustomerRegistered customerRegistered
    ) {
        try {
            if (!customerRegistered.validate()) return;

            // view 객체 생성
            CustomerReadmodel customerReadmodel = new CustomerReadmodel();
            // view 객체에 이벤트의 Value 를 set 함
            customerReadmodel.setId(
                Long.valueOf(customerRegistered.getUserId())
            );
            customerReadmodel.setName(customerRegistered.getName());
            customerReadmodel.setGender(
                String.valueOf(customerRegistered.getGender())
            );
            customerReadmodel.setBirthDate(customerRegistered.getBirthDate());
            // view 레파지 토리에 save
            customerReadmodelRepository.save(customerReadmodel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
