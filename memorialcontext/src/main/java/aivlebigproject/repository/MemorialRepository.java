package aivlebigproject.repository;

import aivlebigproject.model.Memorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "memorials", path = "memorials")
public interface MemorialRepository
    extends JpaRepository<Memorial, UUID> {}
