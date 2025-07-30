package aivlebigproject.domain;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "funeralPythons",
    path = "funeralPythons"
)
public interface FuneralPythonRepository
    extends PagingAndSortingRepository<FuneralPython, Long> {}
