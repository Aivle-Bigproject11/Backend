package aivlebigproject.domain.repository;

import aivlebigproject.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "memorials", path = "memorials")
public interface MemorialRepository
    extends PagingAndSortingRepository<Memorial, UUID> {}
