package io.kiyar.couriertracking.api.controller;

import io.kiyar.couriertracking.api.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthEndpoint healthEndpoint;
    private final AppProperties appProperties;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        HealthComponent health = healthEndpoint.health();

        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("name", this.appProperties.getName());
        healthInfo.put("version", this.appProperties.getVersion());
        healthInfo.put("status", health.getStatus().getCode());
        healthInfo.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(healthInfo);
    }
}
