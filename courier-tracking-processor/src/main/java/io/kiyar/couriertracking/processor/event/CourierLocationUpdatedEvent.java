package io.kiyar.couriertracking.processor.event;

import lombok.Data;

import java.time.Instant;

@Data
public class CourierLocationUpdatedEvent {
    private String courierId;
    private double lat;
    private double lng;
    private Instant timestamp;
}
