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
                .filter(c -> criteria.getGender() == null || c.getGender().equalsIgnoreCase(criteria.getGender()))
                .filter(c -> matchDisease(c.getDisease(), criteria.getDisease()))
                .filter(c -> criteria.getIsMarried() == null || c.getIsMarried().equals(criteria.getIsMarried()))
                .filter(c -> criteria.getHasChildren() == null || c.getHasChildren().equals(criteria.getHasChildren()))
                .collect(Collectors.toList());
    }

    private boolean matchAge(int age, String ageGroup) {
        if (ageGroup == null) return true;
        switch (ageGroup) {
            case "10대": return age >= 10 && age < 20;
            case "20대": return age >= 20 && age < 30;
            case "30대": return age >= 30 && age < 40;
            case "40대": return age >= 40 && age < 50;
            case "50대": return age >= 50 && age < 60;
            case "60대": return age >= 60 && age < 70;
            case "70대": return age >= 70 && age < 80;
            case "80대": return age >= 80 && age < 90;
            case "90대": return age >= 90 && age < 100;
            default: return true;
        }
    }

    private boolean matchDisease(List<String> diseaseList, String diseaseCondition) {
        if (diseaseCondition == null) return true;
        if (diseaseCondition.equals("무")) return diseaseList == null || diseaseList.isEmpty();
        if (diseaseCondition.equals("유")) return diseaseList != null && !diseaseList.isEmpty();
        return true;
    }
}