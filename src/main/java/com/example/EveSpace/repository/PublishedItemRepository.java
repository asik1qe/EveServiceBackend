package com.example.EveSpace.repository;

import com.example.EveSpace.entity.PublishedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublishedItemRepository extends JpaRepository<PublishedItem,Long> {
}
