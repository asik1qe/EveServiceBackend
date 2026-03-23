package com.example.EveSpace.db_services;

import com.example.EveSpace.DTO.CorporationDto;
import com.example.EveSpace.entity.CorporationEntity;
import com.example.EveSpace.repository.CorporationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DBCorporationService {
    private final CorporationRepository corporationRepo;

    public DBCorporationService(CorporationRepository corporationRepo) {
        this.corporationRepo = corporationRepo;
    }


    @Transactional
    public void upsertCorporation(CorporationDto dto) {
        CorporationEntity corporation = corporationRepo.findById(dto.corporation_id())
                .orElseGet(() -> new CorporationEntity(
                        dto.corporation_id(),
                        dto.name(),
                        dto.px256x256()
                ));

        corporation.setName(dto.name());
        corporation.setIcon256Url(dto.px256x256());

        corporationRepo.save(corporation);
    }

    @Transactional(readOnly = true)
    public List<CorporationDto> getAllCorporations() {
        return corporationRepo.findAll().stream()
                .map(corp -> new CorporationDto(corp.getCorporationId(),
                        corp.getName(),
                        corp.getIcon256Url()))
                .toList();
    }
}
