package io.kiyar.couriertracking.api.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class CourierLocationUpdatedEvent {
    private String courierId;
    private double lat;
    private double lng;
    private Instant timestamp;
}
