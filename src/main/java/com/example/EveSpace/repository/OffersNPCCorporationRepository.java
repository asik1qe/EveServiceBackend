package com.example.EveSpace.repository;

import com.example.EveSpace.entity.OffersNPCCorporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffersNPCCorporationRepository extends JpaRepository<OffersNPCCorporation,Long> {
    List<OffersNPCCorporation> findAllByCorporationId(long corporationId);
}
