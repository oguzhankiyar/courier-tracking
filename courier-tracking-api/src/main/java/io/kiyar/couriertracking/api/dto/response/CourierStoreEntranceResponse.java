package io.kiyar.couriertracking.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class CourierStoreEntranceResponse {

    private String storeId;
    private Instant enteredAt;
}