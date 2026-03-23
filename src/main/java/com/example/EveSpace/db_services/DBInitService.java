package com.example.EveSpace.db_services;

import com.example.EveSpace.DTO.*;
import com.example.EveSpace.script_api_eve_online.RequestManager;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import java.util.List;

@Component
public class DBInitService implements CommandLineRunner {
    private final DBOfferService dbOfferService;
    private final DBCorporationService dbCorporationService;
    private final DBItemService dbItemService;
    private final RequestManager requestManager;
    private final Logger log = LoggerFactory.getLogger(DBInitService.class);

    public DBInitService(DBOfferService dbOfferService, RequestManager requestManager,
                         DBCorporationService dbCorporationService, DBItemService dbItemService) {
        this.dbOfferService = dbOfferService;
        this.requestManager = requestManager;
        this.dbCorporationService = dbCorporationService;
        this.dbItemService = dbItemService;
    }

    @Override
    public void run(String... args) {
        initDB();
    }

    public void initDB() {
        initCorporations();
        initOffersCorporations();
        initItem();
        log.info("DB update");
    }

    // Метод проверяет заполнение бд корпораций
    private void initCorporations() {
        if (dbCorporationService.getAllCorporations().isEmpty()) {
            log.info("Corporations NOT found");
            List<CorporationDto> corporationDtos = requestManager.requestCorporations();
            if (corporationDtos == null || corporationDtos.isEmpty()) {
                throw new IllegalStateException("Corporation list is empty after API load");
            }
            for (CorporationDto corporationDto : corporationDtos) {
                if (corporationDto != null) {
                    dbCorporationService.upsertCorporation(corporationDto);
                }
            }
            if (dbCorporationService.getAllCorporations().isEmpty()) {
                log.error("API ERROR Corporations NOT update");
            }
        } else {
            log.info("Corporations already exist");
        }
    }

    // Метод проверяет заполнение бд офферов корпораций
    private void initOffersCorporations() {
        if (dbOfferService.getAllOffers().isEmpty()) {
            log.info("Corporation offers NOT found");
            List<OffersNpcCorporationDTO> corporationDtos = requestManager.requestOffersNpcCorporations();
            if (corporationDtos == null || corporationDtos.isEmpty()) {
                throw new IllegalStateException("Offer list is empty after API load");
            }
            for (OffersNpcCorporationDTO offersNpcCorporationDTO : corporationDtos) {
                if (offersNpcCorporationDTO != null) {
                    dbOfferService.upsertOfferNCPCorporation(offersNpcCorporationDTO);
                }
            }
            if (dbOfferService.getAllOffers().isEmpty()) {
                log.error("API ERROR offers NOT update");
            }
        } else {
            log.info("Corporation offers already exist");
        }
    }

    // Метод проверяет заполнение бд всех предметов в игре
    private void initItem() {
        if (dbItemService.getAllItems().isEmpty()) {
            log.info("Items NOT found");
            List<ItemDTO> itemDTOS = requestManager.requestAllItems();
            if (itemDTOS == null || itemDTOS.isEmpty()) {
                throw new IllegalStateException("Item list is empty after API load");
            }
            for (ItemDTO itemDTO : itemDTOS) {
                if (itemDTO != null) {
                    dbItemService.upsertItem(itemDTO);
                }
            }
            if (dbItemService.getAllItems().isEmpty()) {
                log.error("API ERROR items NOT update");
            }
        } else {
            log.info("Items already exist");
        }
    }
}
