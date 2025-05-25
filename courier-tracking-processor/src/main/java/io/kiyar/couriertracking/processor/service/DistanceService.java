package io.kiyar.couriertracking.processor.service;

import io.kiyar.couriertracking.processor.model.Courier;
import io.kiyar.couriertracking.processor.repository.CourierRepository;
import io.kiyar.couriertracking.processor.strategy.DistanceCalculatorStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistanceService {

    private final CourierRepository courierRepository;
    private final DistanceCalculatorStrategy distanceCalculator;

    public void processLocationUpdate(String courierId, Double lat, Double lng, Instant timestamp) {
        Optional<Courier> courierOpt = courierRepository.findByEntityId(courierId);

        if (courierOpt.isEmpty()) {
            log.error("Courier with id {} not found", courierId);
            throw new RuntimeException("Courier not found: " + courierId);
        }

        Courier courier = courierOpt.get();

        Double lastLocationLat = courier.getLastLocationLat();
        Double lastLocationLng = courier.getLastLocationLng();

        boolean isLastLocationExists = lastLocationLat != null && lastLocationLng != null;

        if (!isLastLocationExists) {
            log.info("Courier with id {} does not have last location info", courierId);
            return;
        }

        double distance = distanceCalculator.calculate(lastLocationLat, lastLocationLng, lat, lng);

        courier.setLastLocationLat(lat);
        courier.setLastLocationLng(lng);
        courier.setUpdatedAt(Instant.now());

        courierRepository.save(courier);

        log.info("Saved courier location for {} with distance {} m", courierId, distance);
    }
}