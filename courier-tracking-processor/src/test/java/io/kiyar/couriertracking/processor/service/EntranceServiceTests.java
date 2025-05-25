package io.kiyar.couriertracking.processor.service;

import com.uber.h3core.H3Core;
import io.kiyar.couriertracking.processor.cache.HexagonStoreCache;
import io.kiyar.couriertracking.processor.cache.dto.StoreCache;
import io.kiyar.couriertracking.processor.config.EntranceConfig;
import io.kiyar.couriertracking.processor.config.HexagonConfig;
import io.kiyar.couriertracking.processor.model.CourierStoreEntrance;
import io.kiyar.couriertracking.processor.repository.CourierStoreEntranceRepository;
import io.kiyar.couriertracking.processor.strategy.DistanceCalculatorStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntranceServiceTests {

    private CourierStoreEntranceRepository entranceRepository;
    private DistanceCalculatorStrategy distanceCalculator;
    private HexagonStoreCache storeCache;
    private H3Core h3Core;

    private EntranceService entranceService;

    @BeforeEach
    void setUp() {
        entranceRepository = mock(CourierStoreEntranceRepository.class);
        distanceCalculator = mock(DistanceCalculatorStrategy.class);
        storeCache = mock(HexagonStoreCache.class);

        HexagonConfig hexagonConfig = new HexagonConfig();
        hexagonConfig.setResolution(9);
        hexagonConfig.setMaxDistanceMeters(100);

        EntranceConfig entranceConfig = new EntranceConfig();
        entranceConfig.setReentryThresholdSeconds(60);

        h3Core = mock(H3Core.class);

        entranceService = new EntranceService(
                entranceRepository,
                distanceCalculator,
                storeCache,
                hexagonConfig,
                entranceConfig,
                h3Core
        );
    }

    @Test
    void shouldSaveEntranceWhenWithinDistanceAndNoRecentEntry() throws Exception {
        String courierId = "ok-1";
        double lat = 40.99401536081996, lng = 29.122665069002927;
        Instant timestamp = Instant.now();

        String h3Index = "hex-1";
        StoreCache store = new StoreCache("s1", "Test Store", 40, 29);

        when(h3Core.geoToH3Address(lat, lng, 9)).thenReturn(h3Index);
        when(storeCache.getStoresByHexagon(h3Index)).thenReturn(List.of(store));
        when(distanceCalculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(50.0);
        when(entranceRepository.findFirstByCourierIdAndStoreIdOrderByEnteredAtDesc(courierId, "s1"))
                .thenReturn(Optional.empty());

        entranceService.processEntrance(courierId, lat, lng, timestamp);

        ArgumentCaptor<CourierStoreEntrance> captor = ArgumentCaptor.forClass(CourierStoreEntrance.class);
        verify(entranceRepository).save(captor.capture());

        CourierStoreEntrance saved = captor.getValue();
        assertEquals(courierId, saved.getCourierId());
        assertEquals("s1", saved.getStoreId());
        assertEquals(timestamp, saved.getEnteredAt());
    }

    @Test
    void shouldSkipIfDistanceTooFar() throws Exception {
        String courierId = "c2";
        double lat = 40.0, lng = 29.0;
        Instant timestamp = Instant.now();

        StoreCache store = new StoreCache("s2", "Far Store", 42.0, 31.0);

        when(h3Core.geoToH3Address(anyDouble(), anyDouble(), anyInt())).thenReturn("hex321");
        when(storeCache.getStoresByHexagon("hex321")).thenReturn(List.of(store));
        when(distanceCalculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(150.0);

        entranceService.processEntrance(courierId, lat, lng, timestamp);

        verify(entranceRepository, never()).save(any());
    }

    @Test
    void shouldSkipIfReentryTooSoon() throws Exception {
        String courierId = "c3";
        Instant now = Instant.now();

        StoreCache store = new StoreCache("s3", "Metropol", 40.99401536081996, 29.122665069002927);

        when(h3Core.geoToH3Address(anyDouble(), anyDouble(), anyInt())).thenReturn("hex");
        when(storeCache.getStoresByHexagon("hex")).thenReturn(List.of(store));
        when(distanceCalculator.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(30.0);
        when(entranceRepository.findFirstByCourierIdAndStoreIdOrderByEnteredAtDesc(courierId, "s3"))
                .thenReturn(Optional.of(new CourierStoreEntrance(courierId, "s3", now.minusSeconds(30))));

        entranceService.processEntrance(courierId, 40.994015360, 29.122665069, now);

        verify(entranceRepository, never()).save(any());
    }
}
