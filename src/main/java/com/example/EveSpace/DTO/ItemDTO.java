package com.example.EveSpace.DTO;

public record ItemDTO(
        long type_id, // id предмета
        String name, // название предмета
        boolean published, // доступность для покупки
        String icon // иконка предмета
) {
}
