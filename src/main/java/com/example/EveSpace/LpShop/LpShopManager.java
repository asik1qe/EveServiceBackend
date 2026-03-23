package com.example.EveSpace.LpShop;

import com.example.EveSpace.DTO.CorporationDto;
import com.example.EveSpace.DTO.ResponseLpShopDTO;
import com.example.EveSpace.db_services.DBOfferService;
import com.example.EveSpace.redis.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class LpShopManager {
    private final DBOfferService dbService;
    private final ExecutorService executor;
    private final LpShopService lpShopService;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(LpShopManager.class);

    public LpShopManager (DBOfferService dbService, @Qualifier("smallExecutor") ExecutorService executor,
                          LpShopService lpShopService,  RedisCache redisCache,
                          ObjectMapper objectMapper) {
        this.dbService = dbService;
        this.executor = executor;
        this.lpShopService = lpShopService;
        this.redisCache = redisCache;
        this.objectMapper = objectMapper;
    }

    public String get_rest_redis(long corporation_id) {
        String answer = redisCache.getJson(corporation_id);
        if (answer == null || answer.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Data is not available yet"
            );
        }
        return answer;
    }

    public void calculate_rest_answer_for_LpShop() {
        List<CorporationDto> corporations = dbService.getCorporationsWithOffers();
        log.info("Start calculate rest answer");
        List<CompletableFuture<Void>> futures = corporations.stream()
                .map(corp -> CompletableFuture.runAsync(() -> {
                    List<ResponseLpShopDTO> answer =
                            lpShopService.getAllOffersByCorporation_id(corp.corporation_id());
                    String json;
                    try {
                        json = objectMapper.writeValueAsString(answer);
                    } catch (JsonProcessingException e) {
                        throw new IllegalStateException("JSON serialize failed for corpId=" + corp, e);
                    }

                    redisCache.putJson(corp.corporation_id(), json);
                    }, executor
                )).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("End calculate rest answer");
    }

}