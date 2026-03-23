package com.example.EveSpace.DTO;

import java.util.List;

public record ESIOffersNpcCorporationDTO (
        long type_id, // id предмета который будет получен
        long ak_cost, // стоимость в устаревшей валюте (не нужное поле)
        long isk_cost, // сколько нужно доплатить для получения предметов
        long lp_cost, // цена в единицах LP (цена покупки)
        long offer_id, // id офера в LP Store
        long quantity, // сколько штук будет получено
        List<SupportOffersNPCCorporationDTO> required_items // список необходимых предметов для получения
){
}
