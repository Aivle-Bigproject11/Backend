package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import javax.transaction.Transactional;
import java.util.Optional;

import java.util.Optional;

@Service
public class CustomerInfoViewHandler {

    private final CustomerInfoRepository customerInfoRepository;

    public CustomerInfoViewHandler(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    // CREATE
    @StreamListener(
            target = KafkaProcessor.INPUT,
            condition = "headers['type']=='CustomerRegistered'"
    )
    public void whenCustomerRegistered_then_CREATE_1(@Payload CustomerRegistered e) {
        if (!e.validate()) return;

        CustomerInfo ci = new CustomerInfo();
        ci.setId(e.getCustomerId());
        ci.setName(e.getName());
        ci.setAge(e.getAge());
        ci.setPhone(e.getPhone());
        ci.setJob(e.getJob());
        ci.setAddress(e.getAddress());
        ci.setGender(e.getGender());
        ci.setHasChildren(e.getHasChildren());
        ci.setIsMarried(e.getIsMarried());
        ci.setBirthDate(e.getBirthDate());
        ci.setDisease(e.getDiseaseList()); // List<String> 컨버터

        customerInfoRepository.save(ci);
    }

    // UPDATE
    @Transactional
    @StreamListener(
            target = KafkaProcessor.INPUT,
            condition = "headers['type']=='CustomerUpdated'"
    )
    public void whenCustomerUpdated_then_UPDATE_1(@Payload CustomerUpdated e) {
        if (!e.validate()) return;

        customerInfoRepository.findById(e.getCustomerId()).ifPresent(ci -> {
            // 부분 업데이트: null인 필드는 건드리지 않음
            if (e.getName() != null) ci.setName(e.getName());
            if (e.getAge() != null) ci.setAge(e.getAge());
            if (e.getPhone() != null) ci.setPhone(e.getPhone());
            if (e.getJob() != null) ci.setJob(e.getJob());
            if (e.getAddress() != null) ci.setAddress(e.getAddress());
            if (e.getGender() != null) ci.setGender(e.getGender());
            if (e.getBirthDate() != null) ci.setBirthDate(e.getBirthDate());
            if (e.getHasChildren() != null) ci.setHasChildren(e.getHasChildren());
            if (e.getIsMarried() != null) ci.setIsMarried(e.getIsMarried());
            if (e.getDiseaseList() != null) ci.setDisease(e.getDiseaseList());
            // @Transactional이라 flush 자동
        });
    }

    // DELETE
    @Transactional
    @StreamListener(
            target = KafkaProcessor.INPUT,
            condition = "headers['type']=='CustomerDeleted'"
    )
    public void whenCustomerDeleted_then_DELETE_1(@Payload CustomerDeleted e) {
        if (!e.validate()) return;
        if (customerInfoRepository.existsById(e.getCustomerId())) {
            customerInfoRepository.deleteById(e.getCustomerId());
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