package com.example.EveSpace.DTO;

public record ItemOrdersDTO(
        long order_id, // уникальный id ордера
        long type_id, // id премета
        boolean is_buy_order, //, buy/sell ордер
        double price, // рыночная цена
        long volume_remain, // количество доступное к покупке
        long system_id, // id системы размещения ордера
        long location_id // id станции размещения ордера
) {
}
