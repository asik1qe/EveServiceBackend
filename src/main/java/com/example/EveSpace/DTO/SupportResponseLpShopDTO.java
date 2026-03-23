package com.example.EveSpace.DTO;

public record SupportResponseLpShopDTO(
        String name, // название предмета
        String icon, // иконка предмета
        long quantity, // количество предмета
        double price_isk // рыночная стоимость предмета
) {
}
