package io.kiyar.couriertracking.api.mapper;

import io.kiyar.couriertracking.api.cache.dto.StoreCache;
import io.kiyar.couriertracking.api.dto.response.StoreResponse;
import io.kiyar.couriertracking.api.model.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreResponse toResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getEntityId())
                .name(store.getName())
                .lat(store.getLat())
                .lng(store.getLng())
                .build();
    }

    public StoreCache toCache(Store store) {
        return StoreCache.builder()
                .id(store.getEntityId())
                .name(store.getName())
                .lat(store.getLat())
                .lng(store.getLng())
                .build();
    }
}