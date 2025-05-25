package io.kiyar.couriertracking.api.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StoreCache {

    private String id;
    private String name;
    private double lat;
    private double lng;
}
