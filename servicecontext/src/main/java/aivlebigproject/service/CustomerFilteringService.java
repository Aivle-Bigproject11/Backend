package aivlebigproject.service;

import aivlebigproject.domain.CustomerInfo;
import aivlebigproject.infra.CustomerInfoRepository;
import aivlebigproject.domain.dto.FilterCriteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerFilteringService {

    private final CustomerInfoRepository customerInfoRepository;

    public CustomerFilteringService(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    public List<CustomerInfo> filterCustomers(FilterCriteria criteria) {
        Iterable<CustomerInfo> all = customerInfoRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .filter(c -> matchAge(c.getAge(), criteria.getAgeGroup()))
                .filter(c -> c.getGender().equalsIgnoreCase(criteria.getGender()))
                .filter(c -> matchDisease(c.getDisease(), criteria.getDisease()))
                .filter(c -> matchFamily(c, criteria.getFamily()))
                .collect(Collectors.toList());
    }

    private boolean matchAge(int age, String ageGroup) {
        if (ageGroup == null) return true;
        switch (ageGroup) {
            case "40대": return age >= 40 && age < 50;
            case "50대": return age >= 50 && age < 60;
            case "60대": return age >= 60 && age < 70;
            default: return true;
        }
    }

    private boolean matchDisease(List<String> diseaseList, String diseaseCondition) {
        if (diseaseCondition.equals("무")) return diseaseList == null || diseaseList.isEmpty();
        return diseaseList != null && !diseaseList.isEmpty();
    }

    private boolean matchFamily(CustomerInfo c, String family) {
        switch (family) {
            case "미혼": return !c.getIsMarried();
            case "기혼": return c.getIsMarried() && !c.getHasChildren();
            case "자녀": return c.getIsMarried() && c.getHasChildren();
            default: return true;
        }
    }
}