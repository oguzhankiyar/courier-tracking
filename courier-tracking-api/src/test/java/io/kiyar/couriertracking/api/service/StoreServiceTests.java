package io.kiyar.couriertracking.api.service;

import com.uber.h3core.H3Core;
import io.kiyar.couriertracking.api.cache.HexagonStoreCache;
import io.kiyar.couriertracking.api.cache.dto.StoreCache;
import io.kiyar.couriertracking.api.config.HexagonConfig;
import io.kiyar.couriertracking.api.dto.response.StoreResponse;
import io.kiyar.couriertracking.api.mapper.StoreMapper;
import io.kiyar.couriertracking.api.model.Store;
import io.kiyar.couriertracking.api.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceTests {

    private StoreRepository storeRepository;
    private StoreMapper storeMapper;
    private HexagonStoreCache storeCache;
    private HexagonConfig hexagonConfig;
    private H3Core h3Core;
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        storeRepository = mock(StoreRepository.class);
        storeMapper = mock(StoreMapper.class);
        storeCache = mock(HexagonStoreCache.class);
        hexagonConfig = new HexagonConfig();
        hexagonConfig.setResolution(9);
        hexagonConfig.setRingRadius(1);
        h3Core = mock(H3Core.class);

        storeService = new StoreService(storeRepository, storeMapper, storeCache, hexagonConfig, h3Core);
    }

    @Test
    void shouldMapAndReturnAllStores() {
        Store store = new Store("Metropol", 40.99401536081996, 29.122665069002927);
        store.setEntityId("mp-1");
        StoreResponse response = new StoreResponse("mp-1", "Metropol", 40.99401536081996, 29.122665069002927);

        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(storeMapper.toResponse(store)).thenReturn(response);

        List<StoreResponse> result = storeService.getAllStores();
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void shouldRefreshCacheWithH3Indexes() {
        Store store = new Store("Metropol", 40.99401536081996, 29.122665069002927);
        store.setEntityId("s1");
        StoreCache storeCacheDto = new StoreCache("mp-1", "Metropol", 40.99401536081996, 29.122665069002927);
        String centerHexagon = "hex-2";
        String leftHexagon = "hex-1";
        String rightHexagon = "hex-3";

        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(h3Core.geoToH3Address(anyDouble(), anyDouble(), anyInt())).thenReturn(centerHexagon);
        when(h3Core.kRing(centerHexagon, 1)).thenReturn(List.of(leftHexagon, centerHexagon, rightHexagon));
        when(storeMapper.toCache(store)).thenReturn(storeCacheDto);

        storeService.refreshCache();

        ArgumentCaptor<Map<String, List<StoreCache>>> captor = ArgumentCaptor.forClass(Map.class);
        verify(storeCache).setAll(captor.capture());

        Map<String, List<StoreCache>> result = captor.getValue();
        assertEquals(3, result.size());
        assertTrue(result.get("hex-1").contains(storeCacheDto));
    }
}