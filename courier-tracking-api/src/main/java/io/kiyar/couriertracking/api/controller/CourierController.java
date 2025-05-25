package io.kiyar.couriertracking.api.controller;

import io.kiyar.couriertracking.api.dto.request.CreateCourierRequest;
import io.kiyar.couriertracking.api.dto.response.CourierResponse;
import io.kiyar.couriertracking.api.dto.response.CourierStoreEntranceResponse;
import io.kiyar.couriertracking.api.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @GetMapping
    public ResponseEntity<List<CourierResponse>> getAll() {
        return ResponseEntity.ok(courierService.getAllCouriers());
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<CourierResponse> getById(@PathVariable String courierId) {
        return courierService
                .getCourierById(courierId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{courierId}/entrances")
    public ResponseEntity<List<CourierStoreEntranceResponse>> getCourierEntrances(@PathVariable String courierId) {
        List<CourierStoreEntranceResponse> entrances = courierService.getEntrancesByCourierId(courierId);
        return ResponseEntity.ok(entrances);
    }

    @PostMapping
    public ResponseEntity<CourierResponse> create(@Valid @RequestBody CreateCourierRequest request) {
        CourierResponse response = courierService.createCourier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
