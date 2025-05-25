package io.kiyar.couriertracking.api.service;

import io.kiyar.couriertracking.api.config.LocationConfig;
import io.kiyar.couriertracking.api.dto.request.UpdateCourierLocationRequest;
import io.kiyar.couriertracking.api.event.CourierLocationUpdatedEvent;
import io.kiyar.couriertracking.api.exception.InvalidTimestampException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final KafkaTemplate<String, CourierLocationUpdatedEvent> kafkaTemplate;
    private final LocationConfig locationConfig;

    public void publishLocation(UpdateCourierLocationRequest request) {
        Instant systemTimestamp = Instant.now();
        Instant requestTimestamp = request.getTimestamp();
        long diff = Duration.between(requestTimestamp, systemTimestamp).getSeconds();

        if (diff < 0 || diff > locationConfig.getTimestampToleranceSeconds()) {
            throw new InvalidTimestampException("Timestamp is out of allowed range.");
        }

        CourierLocationUpdatedEvent event = CourierLocationUpdatedEvent.builder()
                .courierId(request.getCourierId())
                .lat(request.getLat())
                .lng(request.getLng())
                .timestamp(request.getTimestamp())
                .build();

        kafkaTemplate.send("courier-location-updates", event.getCourierId(), event);
    }
}
