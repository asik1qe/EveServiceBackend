package com.example.EveSpace.repository;

import com.example.EveSpace.entity.CorporationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorporationRepository extends JpaRepository<CorporationEntity, Long> {
    @Query("""
            select c
            from CorporationEntity c
            where exists (
                select 1
                from OffersNPCCorporation o
                where o.corporationId = c.corporationId
                )
            """)
    List<CorporationEntity> findCorporationsWithOffers();
}
