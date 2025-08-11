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
public class CustomerReadModelViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private CustomerReadModelRepository customerReadModelRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCustomerRegistered_then_CREATE_1(
        @Payload CustomerRegistered customerRegistered
    ) {
        try {
            if (!customerRegistered.validate()) return;

            // view 객체 생성
            CustomerReadModel customerReadModel = new CustomerReadModel();
            // view 객체에 이벤트의 Value 를 set 함
            customerReadModel.setCustomerId(customerRegistered.getCustomerId());
            customerReadModel.setCustomerName(customerRegistered.getName());
            customerReadModel.setCustomerRrn(customerRegistered.getRrn());
            customerReadModel.setCustomerAge(customerRegistered.getAge());
            customerReadModel.setCustomerGender(customerRegistered.getGender());
            customerReadModel.setCustomerAddress(
                customerRegistered.getAddress()
            );
            customerReadModel.setCustomerPhone(customerRegistered.getPhone());
            customerReadModel.setCustomerJob(customerRegistered.getJob());
            customerReadModel.setCustomerHasChildren(
                customerRegistered.getHasChildren()
            );
            customerReadModel.setCustomerIsMarried(
                customerRegistered.getIsMarried()
            );
            customerReadModel.setCustomerBirthOfDate(
                customerRegistered.getBirthOfDate()
            );
            // view 레파지 토리에 save
            customerReadModelRepository.save(customerReadModel);
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
            Optional<CustomerReadModel> customerReadModelOptional = customerReadModelRepository.findByCustomerId(
                customerUpdated.getCustomerId()
            );

            if (customerReadModelOptional.isPresent()) {
                CustomerReadModel customerReadModel = customerReadModelOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                customerReadModel.setCustomerName(customerUpdated.getName());
                customerReadModel.setCustomerRrn(customerUpdated.getRrn());
                customerReadModel.setCustomerAge(customerUpdated.getAge());
                customerReadModel.setCustomerBirthOfDate(
                    customerUpdated.getBirthOfDate()
                );
                customerReadModel.setCustomerGender(
                    customerUpdated.getGender()
                );
                customerReadModel.setCustomerAddress(
                    customerUpdated.getAddress()
                );
                customerReadModel.setCustomerPhone(customerUpdated.getPhone());
                customerReadModel.setCustomerJob(customerUpdated.getJob());
                customerReadModel.setCustomerHasChildren(
                    customerUpdated.getHasChildren()
                );
                customerReadModel.setCustomerIsMarried(
                    customerUpdated.getIsMarried()
                );
                // view 레파지 토리에 save
                customerReadModelRepository.save(customerReadModel);
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
            customerReadModelRepository.deleteById(
                customerDeleted.getCustomerId()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}