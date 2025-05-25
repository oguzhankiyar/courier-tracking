package io.kiyar.couriertracking.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCourierRequest {

    @NotBlank
    private String name;
}
