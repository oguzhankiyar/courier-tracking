package io.kiyar.couriertracking.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StoreResponse {

    private String id;
    private String name;
    private Double lat;
    private Double lng;
}
