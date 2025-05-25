package io.kiyar.couriertracking.api.service;

import io.kiyar.couriertracking.api.config.LocationConfig;
import io.kiyar.couriertracking.api.dto.request.UpdateCourierLocationRequest;
import io.kiyar.couriertracking.api.event.CourierLocationUpdatedEvent;
import io.kiyar.couriertracking.api.exception.InvalidTimestampException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTests {

    private KafkaTemplate<String, CourierLocationUpdatedEvent> kafkaTemplate;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);

        LocationConfig locationConfig = new LocationConfig();
        locationConfig.setTimestampToleranceSeconds(60);

        locationService = new LocationService(kafkaTemplate, locationConfig);
    }

    @Test
    void shouldSendEventToKafkaWhenTimestampIsValid() {
        UpdateCourierLocationRequest req = new UpdateCourierLocationRequest();
        req.setCourierId("ok-1");
        req.setLat(40.99401536081996);
        req.setLng(29.122665069002927);
        req.setTimestamp(Instant.now());

        locationService.publishLocation(req);

        verify(kafkaTemplate).send(eq("courier-location-updates"), eq("ok-1"), any(CourierLocationUpdatedEvent.class));
    }

    @Test
    void shouldThrowInvalidTimestampExceptionWhenOutsideTolerance() {
        UpdateCourierLocationRequest req = new UpdateCourierLocationRequest();
        req.setCourierId("ok-1");
        req.setLat(40.99401536081996);
        req.setLng(29.122665069002927);
        req.setTimestamp(Instant.now().minusSeconds(600));

        assertThrows(InvalidTimestampException.class, () -> {
            locationService.publishLocation(req);
        });

        verify(kafkaTemplate, never()).send(any(), any(), any());
    }
}
