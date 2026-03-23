package com.example.EveSpace.DTO;

public record CorporationDto(
        long corporation_id, // id корпорации
        String name, // название
        String px256x256 // иконка
) {
}