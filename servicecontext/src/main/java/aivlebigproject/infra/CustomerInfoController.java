package aivlebigproject.infra;

import aivlebigproject.domain.CustomerInfo;
import aivlebigproject.domain.dto.FilterCriteria;
import aivlebigproject.service.CustomerFilteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/customer-infos")
public class CustomerInfoController {

    private final CustomerFilteringService customerFilteringService;
    private final CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerInfoController(
            CustomerFilteringService customerFilteringService,
            CustomerInfoRepository customerInfoRepository
    ) {
        this.customerFilteringService = customerFilteringService;
        this.customerInfoRepository = customerInfoRepository;
    }

    /**
     * 전체 고객 리스트 반환
     */
    @GetMapping
    public List<CustomerInfo> getAllCustomerInfos() {
        Iterable<CustomerInfo> iterable = customerInfoRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * 필터 조건에 해당하는 고객 리스트 반환
     */
    @GetMapping("/filter")
    public List<CustomerInfo> getFilteredCustomerInfos(
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String disease,
            @RequestParam(required = false) Boolean isMarried,
            @RequestParam(required = false) Boolean hasChildren
    ) {
        FilterCriteria criteria = new FilterCriteria(ageGroup, gender, disease, isMarried, hasChildren);
        return customerFilteringService.filterCustomers(criteria);
    }
}
