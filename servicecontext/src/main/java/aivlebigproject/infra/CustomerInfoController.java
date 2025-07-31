package aivlebigproject.infra;

import aivlebigproject.domain.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/customer-infos")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @GetMapping
    public List<CustomerInfo> getAllCustomerInfos() {
        Iterable<CustomerInfo> iterable = customerInfoRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}