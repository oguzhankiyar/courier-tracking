package io.kiyar.couriertracking.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStoreRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}