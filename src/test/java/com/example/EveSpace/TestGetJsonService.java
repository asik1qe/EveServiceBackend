package com.example.EveSpace;

import com.example.EveSpace.DTO.CorporationDto;
import com.example.EveSpace.DTO.ItemDTO;
import com.example.EveSpace.DTO.ItemOrdersDTO;
import com.example.EveSpace.DTO.OffersNpcCorporationDTO;
import com.example.EveSpace.script_api_eve_online.GetJsonServes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
class GetJsonServesTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private GetJsonServes service;

    @Test
    void getNPCСorporation_shouldReturnArray() {
        long[] expected = {1L, 2L, 3L};

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/corporations/npccorps/")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(long[].class)).thenReturn(Mono.just(expected));

        long[] result = service.getNPCСorporation();

        assertArrayEquals(expected, result);
    }

    @Test
    void getNPCСorporation_shouldThrowIllegalStateException_whenWebClientFails() {
        WebClientResponseException ex = WebClientResponseException.create(
                404, "Not Found", HttpHeaders.EMPTY, null, null
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/corporations/npccorps/")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(long[].class)).thenReturn(Mono.error(ex));

        IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> service.getNPCСorporation()
        );

        assertTrue(result.getMessage().contains("Failed to get NPC corporations"));
        assertTrue(result.getMessage().contains("404"));
    }

    @Test
    void getOffersNPCCorporation_shouldReturnEmptyList() {
        long corporationId = 1000001L;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/loyalty/stores/{id}/offers/", corporationId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(any(Class.class))).thenReturn(Flux.empty());

        List<OffersNpcCorporationDTO> result = service.getOffersNPCCorporation(corporationId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllItemId_shouldReturnAllIdsFromAllPages() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Pages", "2");
        ResponseEntity<String> entity = new ResponseEntity<>("", headers, HttpStatusCode.valueOf(200));

        when(webClient.get()).thenReturn(requestHeadersUriSpec, requestHeadersUriSpec, requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri("/universe/types/")).thenReturn(requestHeadersSpec);
        when(requestHeadersUriSpec.uri("/universe/types/?page={num_page}", 1)).thenReturn(requestHeadersSpec);
        when(requestHeadersUriSpec.uri("/universe/types/?page={num_page}", 2)).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(entity));
        when(responseSpec.bodyToMono(long[].class))
                .thenReturn(Mono.just(new long[]{10L, 20L}))
                .thenReturn(Mono.just(new long[]{30L}));

        List<Long> result = service.getAllItemId();

        assertEquals(List.of(10L, 20L, 30L), result);
    }

    @Test
    void checkItemPublishedById_shouldReturnNull_whenItemIsNull() {
        long itemId = 123L;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/universe/types/{type_id}/", itemId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ItemDTO.class)).thenReturn(Mono.empty());

        ItemDTO result = service.checkItemPublishedById(itemId);

        assertNull(result);
    }

    @Test
    void getAllRegion_shouldReturnArray() {
        int[] expected = {10000002, 10000043};

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/universe/regions/")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(int[].class)).thenReturn(Mono.just(expected));

        int[] result = service.getAllRegion();

        assertArrayEquals(expected, result);
    }

    @Test
    void getCountPageInRegion_shouldReturnPageCount() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Pages", "5");
        ResponseEntity<String> entity = new ResponseEntity<>("", headers, HttpStatusCode.valueOf(200));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(entity));

        int result = service.getCountPageInRegion(10000002);

        assertEquals(5, result);
    }

    @Test
    void getOnePageOrdersInRegion_shouldReturnEmptyList() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ItemOrdersDTO.class)).thenReturn(Flux.empty());

        List<ItemOrdersDTO> result = service.getOnePageOrdersInRegion(10000002, 1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNpcCorporationDtoById_shouldReturnCorporationDto() {
        long corporationId = 12345L;

        Map<String, Object> corporationInfo = Map.of("name", "Test Corp");
        Map<String, Object> iconsInfo = Map.of("px256x256", "https://image.test/icon.png");

        when(webClient.get()).thenReturn(requestHeadersUriSpec, requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri("/corporations/{corporation_id}/", corporationId)).thenReturn(requestHeadersSpec);
        when(requestHeadersUriSpec.uri("/corporations/{corporation_id}/icons/", corporationId)).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class))
                .thenReturn(Mono.just(corporationInfo))
                .thenReturn(Mono.just(iconsInfo));

        CorporationDto result = service.getNpcCorporationDtoById(corporationId);

        assertEquals(corporationId, result.corporation_id());
        assertEquals("Test Corp", result.name());
        assertEquals("https://image.test/icon.png", result.px256x256());
    }

    @Test
    void getNpcCorporationDtoById_shouldThrow_whenCorporationInfoIsNull() {
        long corporationId = 12345L;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/corporations/{corporation_id}/", corporationId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        IllegalStateException result = assertThrows(
                IllegalStateException.class,
                () -> service.getNpcCorporationDtoById(corporationId)
        );

        assertEquals("Corporation info not found for id " + corporationId, result.getMessage());
    }
}
