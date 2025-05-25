package io.kiyar.couriertracking.api.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private long timestamp;
}
