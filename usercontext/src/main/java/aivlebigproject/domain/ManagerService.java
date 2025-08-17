package aivlebigproject.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ManagerService {

  private final ManagerRepository managerRepository;

  @Autowired
  public ManagerService(ManagerRepository managerRepository) {
    this.managerRepository = managerRepository;
  }

  public boolean isLoginIdDuplicate(String loginId) {
    return managerRepository.findByLoginId(loginId).isPresent();
  }

  public Optional<String> findManagerLoginId(String name, String email) {
    Optional<Manager> foundManager = managerRepository.findByNameAndEmail(name, email);

    if (foundManager.isPresent()) {
      return Optional.of(foundManager.get().getLoginId());
    } else {
      return Optional.empty();
    }
  }
  public List<Manager> searchByName(String name) {
    return managerRepository.findByNameContaining(name);
  }
    
  public List<Manager> searchByPhone(String phone) {
    return managerRepository.findByPhoneContaining(phone);
  }

  public Optional<Manager> searchByLoginId(String loginId) {
    return managerRepository.findByLoginId(loginId);
  }

  public Optional<Manager> searchByEmail(String email) {
    return managerRepository.findByEmail(email);
  }
}
