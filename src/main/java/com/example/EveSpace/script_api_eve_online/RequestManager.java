package com.example.EveSpace.script_api_eve_online;

import com.example.EveSpace.DTO.CorporationDto;
import com.example.EveSpace.DTO.ItemDTO;
import com.example.EveSpace.DTO.ItemOrdersDTO;
import com.example.EveSpace.DTO.OffersNpcCorporationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class RequestManager {
    private final GetJsonServes getJsonServes;
    private final ExecutorService apiExecutor;

    public RequestManager(GetJsonServes getJsonServes,
                          @Qualifier("apiExecutor") ExecutorService executor) {
        this.getJsonServes = getJsonServes;
        this.apiExecutor = executor;
    }

    // получение всех id корпораций
    public List<CorporationDto> requestCorporations() {
        final long[] allIdCorporation;
        try {
            allIdCorporation = getJsonServes.getNPCСorporation();
            if (allIdCorporation == null) {
                log.error("API returned null for corporations");
                throw new IllegalStateException("API returned null for corporations");
            }
            if (allIdCorporation.length == 0) {
                log.warn("API returned empty corporation id list");
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            log.error("Failed to request corporation ids", ex);
            throw new IllegalStateException("Failed to request corporation ids", ex);
        }

        List<CompletableFuture<CorporationDto>> futures = Arrays.stream(allIdCorporation)
                .mapToObj(id -> CompletableFuture.supplyAsync(
                                        () -> getJsonServes.getNpcCorporationDtoById(id),
                                        apiExecutor
                                )
                                .exceptionally(ex -> {
                                    log.error("Failed to load corporation {}", id, ex);
                                    return null;
                                })
                ).toList();

        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (CompletionException ex) {
            log.error("Failed while collecting corporation data", ex);
            throw new IllegalStateException("Failed while collecting corporation data", ex);
        }
    }

    // получение всех офферов npc корпораций
    public List<OffersNpcCorporationDTO> requestOffersNpcCorporations() {
        final long[] allIdCorporation;
        try {
            allIdCorporation = getJsonServes.getNPCСorporation();
            if (allIdCorporation == null) {
                log.error("API returned null for corporations");
                throw new IllegalStateException("API returned null for corporations");
            }
            if (allIdCorporation.length == 0) {
                log.warn("API returned empty corporation id list");
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            log.error("Failed to request corporation ids for offers", ex);
            throw new IllegalStateException("Failed to request corporation ids for offers", ex);
        }

        List<CompletableFuture<List<OffersNpcCorporationDTO>>> futures =
                Arrays.stream(allIdCorporation)
                        .mapToObj(id -> CompletableFuture.supplyAsync(
                                        () -> getJsonServes.getOffersNPCCorporation(id),
                                        apiExecutor
                                )
                                .exceptionally(ex -> {
                                    log.error("Failed to load offers for corporation {}", id, ex);
                                    return Collections.emptyList();
                                }))
                        .toList();

        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .toList();
        } catch (CompletionException ex) {
            log.error("Failed while collecting corporation offers", ex);
            throw new IllegalStateException("Failed while collecting corporation offers", ex);
        }
    }

    // получение всех доступных id игровых предметов
    public List<ItemDTO> requestAllItems() {
        final long[] allItemId;
        try {
            List<Long> itemIds = getJsonServes.getAllItemId();
            if (itemIds == null) {
                log.error("API returned null for items");
                throw new IllegalStateException("API returned null for items");
            }
            if (itemIds.isEmpty()) {
                log.warn("API returned empty item id list");
                return Collections.emptyList();
            }
            allItemId = itemIds.stream()
                    .mapToLong(Long::longValue)
                    .toArray();
        } catch (Exception ex) {
            log.error("Failed to request item ids", ex);
            throw new IllegalStateException("Failed to request item ids", ex);
        }

        List<CompletableFuture<ItemDTO>> futures =
                Arrays.stream(allItemId)
                        .mapToObj(id -> CompletableFuture.supplyAsync(
                                        () -> getJsonServes.checkItemPublishedById(id),
                                        apiExecutor)
                                .exceptionally(ex -> {
                                    log.error("Failed to load item {}", id, ex);
                                    return null;
                                }))
                        .toList();

        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (CompletionException ex) {
            log.error("Failed while collecting items", ex);
            throw new IllegalStateException("Failed while collecting items", ex);
        }
    }

    // получение всех ордеров с рынка во всех регионах
    public List<ItemOrdersDTO> requestItemOrdersInAllRegion() {
        final int[] allRegion;
        try {
            allRegion = getJsonServes.getAllRegion();
            if (allRegion == null) {
                log.error("API returned null for regions");
                throw new IllegalStateException("API returned null for regions");
            }
            if (allRegion.length == 0) {
                log.warn("API returned empty region list");
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            log.error("Failed to request region ids", ex);
            throw new IllegalStateException("Failed to request region ids", ex);
        }

        List<CompletableFuture<List<ItemOrdersDTO>>> futures =
                Arrays.stream(allRegion)
                        .mapToObj(id -> CompletableFuture.supplyAsync(
                                        () -> {
                                            int countPage = getJsonServes.getCountPageInRegion(id);
                                            List<ItemOrdersDTO> orders = new ArrayList<>();
                                            for (int i = 1; i <= countPage; i++) {
                                                orders.addAll(getJsonServes.getOnePageOrdersInRegion(id, i));
                                            }
                                            return orders;
                                        }, apiExecutor)
                                .exceptionally(ex -> {
                                    log.error("Failed to load region {}", id, ex);
                                    return Collections.emptyList();
                                }))
                        .toList();

        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (CompletionException ex) {
            log.error("Failed while collecting item orders", ex);
            throw new IllegalStateException("Failed while collecting item orders", ex);
        }
    }
}
