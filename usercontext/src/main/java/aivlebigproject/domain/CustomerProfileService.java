package aivlebigproject.domain; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class CustomerProfileService {

    private final CustomerProfileRepository customerProfileRepository;


    @Autowired
    public CustomerProfileService(CustomerProfileRepository customerProfileRepository) {
        this.customerProfileRepository = customerProfileRepository;
    }

    public boolean isLoginIdDuplicate(String loginId) {
        // 리포지토리의 findByLoginId 메서드를 호출하여 ID 존재 여부를 확인
        return customerProfileRepository.findByLoginId(loginId).isPresent();
    }
}