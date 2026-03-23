package com.example.EveSpace;

import com.example.EveSpace.DTO.*;
import com.example.EveSpace.LpShop.LpShopManager;
import com.example.EveSpace.db_services.DBOfferService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebController {

    final private DBOfferService dbService;
    final private LpShopManager lpShopManager;
    final private Logger log = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/corporations")
    public List<CorporationDto> getCorporations() {
        return dbService.getCorporationsWithOffers();
    }

    @GetMapping(value = "/all_card_offers/{corporationId}", produces = "application/json")
    public String getAllCardOffers(@PathVariable long corporationId) {
        log.info("getAllCardOffers");
        return lpShopManager.get_rest_redis(corporationId);
    }
}
