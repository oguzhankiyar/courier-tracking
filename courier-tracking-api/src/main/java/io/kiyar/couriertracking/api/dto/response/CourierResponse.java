package io.kiyar.couriertracking.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CourierResponse {
    private String id;
    private String name;
    private Double lastLocationLat;
    private Double lastLocationLng;
    private Double totalDistance;
}