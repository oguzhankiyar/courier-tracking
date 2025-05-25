package io.kiyar.couriertracking.api.controller;

import io.kiyar.couriertracking.api.dto.request.UpdateCourierLocationRequest;
import io.kiyar.couriertracking.api.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid UpdateCourierLocationRequest request) {
        locationService.publishLocation(request);
        return ResponseEntity.accepted().build();
    }
}