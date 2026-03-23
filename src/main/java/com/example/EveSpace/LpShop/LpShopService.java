package com.example.EveSpace.LpShop;

import com.example.EveSpace.DTO.*;
import com.example.EveSpace.db_services.DBOfferService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LpShopService {
    final private DBOfferService dbOfferService;

    public LpShopService(DBOfferService dbOfferService) {
        this.dbOfferService = dbOfferService;
    }

    public List<ResponseLpShopDTO> getAllOffersByCorporation_id(long corporation_id) {
        List<ResponseLpShopDTO> answer = dbOfferService.getAllOffersForCardApi(corporation_id);
        return answer;
    }
}
