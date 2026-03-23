package com.example.EveSpace.script_api_eve_online;

import com.example.EveSpace.DTO.*;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GetJsonServes {
    private final WebClient webClient;

    public GetJsonServes(WebClient webClient) {
        this.webClient = webClient;
    }

    // отправка запроса для получение всех NPC корпораций
    public long[] getNPCСorporation() {
        try {
            return webClient.get()
                    .uri("/corporations/npccorps/")
                    .retrieve()
                    .bodyToMono(long[].class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Failed to get NPC corporations: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get NPC corporations", e);
        }
    }

    // отправка запроса для получение офферов всех NPC корпораций
    public List<OffersNpcCorporationDTO> getOffersNPCCorporation(long id_corporation) {
        try {
            return webClient.get()
                    .uri("/loyalty/stores/{id}/offers/", id_corporation)
                    .retrieve()
                    .bodyToFlux(ESIOffersNpcCorporationDTO.class)
                    .map(dto -> new OffersNpcCorporationDTO(
                            id_corporation,
                            dto.type_id(),
                            dto.ak_cost(),
                            dto.isk_cost(),
                            dto.lp_cost(),
                            dto.offer_id(),
                            dto.quantity(),
                            dto.required_items()
                    ))
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException(
                    "Failed to get offers for corporation " + id_corporation + ": " + e.getStatusCode(),
                    e
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get offers for corporation " + id_corporation, e);
        }
    }


    // отправка запроса для получение всех id предметов в игре
    public List<Long> getAllItemId() {
        try {
            int pages = Integer.parseInt(Objects.requireNonNull(webClient.get()
                    .uri("/universe/types/")
                    .retrieve()
                    .toEntity(String.class)
                    .mapNotNull(entity -> entity.getHeaders().getFirst("X-Pages"))
                    .block()));

            List<Long> array_id_item = new ArrayList<>();
            for (int num_page = 1; num_page <= pages; num_page++) {
                try {
                    long[] response = webClient.get()
                            .uri("/universe/types/?page={num_page}", num_page)
                            .retrieve()
                            .bodyToMono(long[].class)
                            .block();
                    if (response != null) {
                        for (long id : response) {
                            array_id_item.add(id);
                        }
                    }
                } catch (WebClientResponseException e) {
                    throw new IllegalStateException("Failed to get items on page " + num_page + ": " + e.getStatusCode(), e);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to get items on page " + num_page, e);
                }
            }
            return array_id_item;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get all item IDs", e);
        }
    }

    // отправка запроса для проверки доступности предмета в игре
    public ItemDTO checkItemPublishedById(long id_item) {
        try {
            ItemDTO item = webClient.get()
                    .uri("/universe/types/{type_id}/", id_item)
                    .retrieve()
                    .bodyToMono(ItemDTO.class)
                    .block();

            if (item == null || !item.published()) {
                return null;
            }

            String imageVariant;

            if (item.name() != null &&
                    item.name().toLowerCase().contains("blueprint")) {

                imageVariant = "bpc";   // для блюпринтов
            } else {
                imageVariant = "icon";  // обычная иконка
            }

            String iconUrl = "https://images.evetech.net/types/"
                    + item.type_id()
                    + "/"
                    + imageVariant
                    + "?size=128";

            return new ItemDTO(
                    item.type_id(),
                    item.name(),
                    item.published(),
                    iconUrl
            );

        } catch (WebClientResponseException e) {
            throw new IllegalStateException(
                    "Failed to check item published status for id "
                            + id_item + ": " + e.getStatusCode(),
                    e
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to check item published status for id "
                            + id_item,
                    e
            );
        }
    }

    // отправка запроса для получения всех игровых регионов
    public int[] getAllRegion() {
        try {
            return webClient.get()
                    .uri("/universe/regions/")
                    .retrieve()
                    .bodyToMono(int[].class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Failed to get regions: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get regions", e);
        }
    }

    // отправка запроса для получения количества страниц с ордерами в регионе
    public int getCountPageInRegion(int id_region) {
        try {
            int pages = Integer.parseInt(Objects.requireNonNull(webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/markets/{region_id}/orders/")
                            .queryParam("order_type", "all")
                            .build(id_region))
                    .retrieve()
                    .toEntity(String.class)
                    .mapNotNull(entity -> entity.getHeaders().getFirst("X-Pages"))
                    .block()));
            return pages;
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Failed to get count page: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get count page", e);
        }
    }

    // отправка запроса для получения всех ордеров с одной страницы
    public List<ItemOrdersDTO> getOnePageOrdersInRegion(int id_region, int page) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/markets/{region_id}/orders/")
                            .queryParam("order_type", "all")
                            .queryParam("page", page)
                            .build(id_region))
                    .retrieve()
                    .bodyToFlux(ItemOrdersDTO.class)
                    .map(dto -> new ItemOrdersDTO(
                            dto.order_id(),
                            dto.type_id(),
                            dto.is_buy_order(),
                            dto.price(),
                            dto.volume_remain(),
                            dto.system_id(),
                            dto.location_id()
                    ))
                    .collectList()
                    .block();
        }
        catch (WebClientResponseException e) {
            throw new IllegalStateException(
                    "Failed to get orders for region " + id_region + " page" + page + " : " + e.getStatusCode(),
                    e
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get orders for region " + id_region, e);
        }
    }

    // отправка запроса для получения информации о корпорации
    public CorporationDto getNpcCorporationDtoById(long corporation_id) {
        Map corporationInfo;
        try {
            corporationInfo = webClient.get()
                    .uri("/corporations/{corporation_id}/", corporation_id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Failed to get corporation info for id " + corporation_id + ": " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get corporation info for id " + corporation_id, e);
        }

        if (corporationInfo == null) {
            throw new IllegalStateException("Corporation info not found for id " + corporation_id);
        }

        String name = (String) corporationInfo.get("name");

        Map iconsInfo;
        try {
            iconsInfo = webClient.get()
                    .uri("/corporations/{corporation_id}/icons/", corporation_id)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("Failed to get corporation icons for id " + corporation_id + ": " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get corporation icons for id " + corporation_id, e);
        }

        if (iconsInfo == null) {
            throw new IllegalStateException("Corporation icons not found for id " + corporation_id);
        }

        String icon256Url = (String) iconsInfo.get("px256x256");

        return new CorporationDto(
                corporation_id,
                name,
                icon256Url
        );
    }
}
