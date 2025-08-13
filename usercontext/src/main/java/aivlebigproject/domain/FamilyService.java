package aivlebigproject.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

@Service
public class FamilyService {
    
     private final FamilyRepository familyRepository;

    @Autowired
    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }
    public boolean isLoginIdDuplicate(String loginId) {
        return familyRepository.findByLoginId(loginId).isPresent();
    }
}
