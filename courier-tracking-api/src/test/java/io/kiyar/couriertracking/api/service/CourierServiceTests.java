package io.kiyar.couriertracking.api.service;

import io.kiyar.couriertracking.api.dto.response.CourierResponse;
import io.kiyar.couriertracking.api.dto.response.CourierStoreEntranceResponse;
import io.kiyar.couriertracking.api.mapper.CourierMapper;
import io.kiyar.couriertracking.api.model.Courier;
import io.kiyar.couriertracking.api.model.CourierStoreEntrance;
import io.kiyar.couriertracking.api.repository.CourierRepository;
import io.kiyar.couriertracking.api.repository.CourierStoreEntranceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierServiceTests {

    private CourierRepository courierRepository;
    private CourierStoreEntranceRepository courierStoreEntranceRepository;
    private CourierMapper courierMapper;
    private CourierService courierService;

    @BeforeEach
    void setUp() {
        courierRepository = mock(CourierRepository.class);
        courierStoreEntranceRepository = mock(CourierStoreEntranceRepository.class);
        courierMapper = mock(CourierMapper.class);
        courierService = new CourierService(courierRepository, courierStoreEntranceRepository, courierMapper);
    }

    @Test
    void shouldReturnCourierResponseWhenCourierExists() {
        String courierId = "ok-1";
        Courier courier = new Courier();
        courier.setEntityId(courierId);
        CourierResponse expected = new CourierResponse(courierId, "Oguzhan", null, null, null);

        when(courierRepository.findByEntityId(courierId)).thenReturn(Optional.of(courier));
        when(courierMapper.toResponse(courier)).thenReturn(expected);

        Optional<CourierResponse> result = courierService.getCourierById(courierId);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void shouldReturnEmptyWhenCourierNotFound() {
        when(courierRepository.findByEntityId("x")).thenReturn(Optional.empty());
        assertTrue(courierService.getCourierById("x").isEmpty());
    }

    @Test
    void shouldReturnEntrancesByCourierId() {
        String courierId = "ok-1";

        CourierStoreEntrance e1 = new CourierStoreEntrance(courierId, "store-1", Instant.now());
        CourierStoreEntrance e2 = new CourierStoreEntrance(courierId, "store-2", Instant.now());

        when(courierStoreEntranceRepository.findByCourierId(courierId))
                .thenReturn(List.of(e1, e2));

        List<CourierStoreEntranceResponse> result = courierService.getEntrancesByCourierId(courierId);

        assertEquals(2, result.size());
    }
}
