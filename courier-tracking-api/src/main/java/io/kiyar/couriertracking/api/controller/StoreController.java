package io.kiyar.couriertracking.api.controller;

import io.kiyar.couriertracking.api.dto.request.CreateStoreRequest;
import io.kiyar.couriertracking.api.dto.response.StoreResponse;
import io.kiyar.couriertracking.api.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<List<StoreResponse>> get() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @PostMapping
    public ResponseEntity<StoreResponse> create(@Valid @RequestBody CreateStoreRequest request) {
        StoreResponse response = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cache/refresh")
    public ResponseEntity<Void> refreshCache() {
        storeService.refreshCache();
        return ResponseEntity.ok().build();
    }
}
