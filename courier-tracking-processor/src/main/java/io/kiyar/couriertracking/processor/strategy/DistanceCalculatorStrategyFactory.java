package io.kiyar.couriertracking.processor.strategy;

import io.kiyar.couriertracking.processor.config.DistanceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistanceCalculatorStrategyFactory {

    private final DistanceConfig config;

    public DistanceCalculatorStrategy getStrategy() {
        return switch (config.getStrategy().toLowerCase()) {
            case "google" -> new GoogleMapsDistanceCalculator();
            case "haversine" -> new HaversineDistanceCalculator();
            default -> throw new IllegalArgumentException("Unknown strategy: " + config.getStrategy());
        };
    }
}
