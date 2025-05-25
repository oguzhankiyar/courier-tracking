package io.kiyar.couriertracking.api.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class UpdateCourierLocationRequest {

    @NotBlank
    private String courierId;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private double lat;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double lng;

    @NotNull
    private Instant timestamp;
}