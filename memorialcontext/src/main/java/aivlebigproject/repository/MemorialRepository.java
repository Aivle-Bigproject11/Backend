package aivlebigproject.repository;

import aivlebigproject.model.Memorial;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "memorials", path = "memorials")
public interface MemorialRepository
    extends JpaRepository<Memorial, UUID> {
    @Query("SELECT COUNT(m) > 0 FROM Memorial m WHERE m.memorialId = :memorialId AND :familyId MEMBER OF m.familyList")
    boolean existsByIdAndFamilyListContaining(@Param("memorialId") UUID memorialId, @Param("familyId") Long familyId);
}
