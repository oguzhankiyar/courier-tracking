package io.kiyar.couriertracking.processor.service;

import io.kiyar.couriertracking.processor.model.Courier;
import io.kiyar.couriertracking.processor.repository.CourierRepository;
import io.kiyar.couriertracking.processor.strategy.DistanceCalculatorStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DistanceServiceTests {

    private CourierRepository courierRepository;
    private DistanceCalculatorStrategy distanceCalculator;
    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        courierRepository = mock(CourierRepository.class);
        distanceCalculator = mock(DistanceCalculatorStrategy.class);
        distanceService = new DistanceService(courierRepository, distanceCalculator);
    }

    @Test
    void shouldCalculateDistanceAndUpdateCourier() {
        String courierId = "courier-ok";
        double oldLat = 40.0;
        double oldLng = 29.0;
        double newLat = 41.0;
        double newLng = 30.0;
        Instant now = Instant.now();

        Courier courier = new Courier();
        courier.setEntityId(courierId);
        courier.setLastLocationLat(oldLat);
        courier.setLastLocationLng(oldLng);

        when(courierRepository.findByEntityId(courierId)).thenReturn(Optional.of(courier));
        when(distanceCalculator.calculate(oldLat, oldLng, newLat, newLng)).thenReturn(120000.0);

        distanceService.processLocationUpdate(courierId, newLat, newLng, now);

        ArgumentCaptor<Courier> courierCaptor = ArgumentCaptor.forClass(Courier.class);
        verify(courierRepository).save(courierCaptor.capture());

        Courier updated = courierCaptor.getValue();
        assertEquals(newLat, updated.getLastLocationLat());
        assertEquals(newLng, updated.getLastLocationLng());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionWhenCourierNotFound() {
        String courierId = "unknown-id";
        when(courierRepository.findByEntityId(courierId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            distanceService.processLocationUpdate(courierId, 40.0, 29.0, Instant.now());
        });
    }

    @Test
    void shouldNotCalculateWhenLastLocationMissing() {
        String courierId = "courier-empty";
        Courier courier = new Courier();
        courier.setEntityId(courierId);

        when(courierRepository.findByEntityId(courierId)).thenReturn(Optional.of(courier));

        distanceService.processLocationUpdate(courierId, 40.0, 29.0, Instant.now());

        verify(distanceCalculator, never()).calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        verify(courierRepository, never()).save(any());
    }
}
