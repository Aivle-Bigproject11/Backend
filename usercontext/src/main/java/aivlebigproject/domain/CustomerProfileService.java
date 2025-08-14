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

}