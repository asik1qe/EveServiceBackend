package com.example.EveSpace.script_api_eve_online;


import com.example.EveSpace.DTO.ItemOrdersDTO;
import com.example.EveSpace.LpShop.LpShopManager;
import com.example.EveSpace.db_services.DBOrderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.List;

@Service
public class UpdateItemOrders {
    RequestManager requestManager;
    LpShopManager lpShopManager;
    DBOrderService dbService;

    @Autowired
    GetJsonServes getJsonServes;
    final static Logger log = LoggerFactory.getLogger(UpdateItemOrders.class);


    public UpdateItemOrders(RequestManager requestManager,
                            LpShopManager lpShopManager, DBOrderService dbService) {
        this.requestManager = requestManager;
        this.lpShopManager = lpShopManager;
        this.dbService = dbService;
    }

    @Scheduled(fixedDelay = 240 * 60 * 1000)
    public void update_runer() {
        log.info("Start update orders");
        try {
            List<ItemOrdersDTO> all_orders = requestManager.requestItemOrdersInAllRegion();

            dbService.updateOrders(all_orders);

            log.info("Finished update orders");
            log.info("Start calculate LpShop");
            lpShopManager.calculate_rest_answer_for_LpShop();
            log.info("Finished calculate LpShop");

        } catch (Exception e) {
            if (dbService.getCountOrders() == 0){
                log.error("Orders not found", e);
                System.exit(1);
            }
            log.warn("Update orders FAILED. Application continues to work with old data.");
            lpShopManager.calculate_rest_answer_for_LpShop();
        }
    }
}
