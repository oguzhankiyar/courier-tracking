package io.kiyar.couriertracking.api.mapper;

import io.kiyar.couriertracking.api.dto.response.CourierStoreEntranceResponse;
import io.kiyar.couriertracking.api.dto.response.CourierResponse;
import io.kiyar.couriertracking.api.model.Courier;
import io.kiyar.couriertracking.api.model.CourierStoreEntrance;
import org.springframework.stereotype.Component;

@Component
public class CourierMapper {

    public CourierResponse toResponse(Courier courier) {
        return CourierResponse.builder()
                .id(courier.getEntityId())
                .name(courier.getName())
                .lastLocationLat(courier.getLastLocationLat())
                .lastLocationLng(courier.getLastLocationLng())
                .totalDistance(courier.getTotalDistance())
                .build();
    }

    public CourierStoreEntranceResponse toStoreEntranceResponse(CourierStoreEntrance courierStoreEntrance) {
        return CourierStoreEntranceResponse.builder()
                .storeId(courierStoreEntrance.getStoreId())
                .enteredAt(courierStoreEntrance.getEnteredAt())
                .build();
    }
}