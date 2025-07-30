package aivlebigprojectminseo.service;

import aivlebigprojectminseo.domain.Tribute;
import aivlebigprojectminseo.domain.repository.TributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TributeService {
    private final TributeRepository tributeRepository;

//    public Tribute createTribute(Tribute tribute) {
//        String keyword =
//        return tributeRepository.save(tribute);
//    }
}
