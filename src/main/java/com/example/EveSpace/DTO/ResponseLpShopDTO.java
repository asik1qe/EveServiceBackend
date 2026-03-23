package com.example.EveSpace.DTO;

import java.util.List;

public record ResponseLpShopDTO (
        String name_get_item, // название получаемого предмета
        String icon_get_item, // иконка получаемого предмета
        long quantity, // количество получаемого предмета
        long lp_cost, // цена в lp получаемого предмета
        long isk_cost, // необходимо потратить сумму в isk для получения предмета
        List<SupportResponseLpShopDTO> required_items, // необходимые предметы
        Double market_price, // цена получаемого предмета на рынке
        Double profit // прибыль
) {}
