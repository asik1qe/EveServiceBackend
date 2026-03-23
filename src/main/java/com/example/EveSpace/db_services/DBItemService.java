package com.example.EveSpace.db_services;

import com.example.EveSpace.DTO.ItemDTO;
import com.example.EveSpace.entity.PublishedItem;
import com.example.EveSpace.repository.PublishedItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DBItemService {
    PublishedItemRepository itemRepo;

    public DBItemService(PublishedItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Transactional
    public void upsertItem(ItemDTO dto) {

        PublishedItem item = itemRepo.findById(dto.type_id())
                .orElseGet(() -> new PublishedItem(
                        dto.type_id(),
                        dto.name(),
                        null,
                        dto.icon()
                ));

        // Обновляем ВСЕ поля
        item.setName(dto.name());
        item.setIcon(dto.icon());

        itemRepo.save(item);
    }

    @Transactional(readOnly = true)
    public List<PublishedItem> getAllItems() {
        return itemRepo.findAll();
    }
}
