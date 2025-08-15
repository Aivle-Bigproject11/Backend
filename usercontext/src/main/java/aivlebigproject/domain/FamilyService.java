package aivlebigproject.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

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

     public List<Family> searchByName(String name) {
        return familyRepository.findByNameContaining(name);
    }
    
    public List<Family> searchByPhone(String phone) {
        return familyRepository.findByPhoneContaining(phone);
    }

    public Optional<Family> searchByLoginId(String loginId) {
        return familyRepository.findByLoginId(loginId);
    }

    public Optional<Family> searchByEmail(String email) {
        return familyRepository.findByEmail(email);
    }

    public Optional<String> findFamilyLoginId(String name, String email) {
        Optional<Family> foundFamily = familyRepository.findByNameAndEmail(name, email);

        if (foundFamily.isPresent()) {
            return Optional.of(foundFamily.get().getLoginId());
        } else {
            return Optional.empty();
        }
    }
    public List<Family> findFamiliesByMemorialId(UUID memorialId) {
    // 필요한 경우 유효성 검사 등 추가 로직을 여기에 작성
        if (memorialId == null) {
            throw new IllegalArgumentException("Memorial ID must not be null.");
        }
        return familyRepository.findByMemorialId(memorialId);
    }
}
