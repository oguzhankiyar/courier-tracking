package io.kiyar.couriertracking.processor.service;

import com.uber.h3core.H3Core;
import io.kiyar.couriertracking.processor.cache.HexagonStoreCache;
import io.kiyar.couriertracking.processor.cache.dto.StoreCache;
import io.kiyar.couriertracking.processor.config.EntranceConfig;
import io.kiyar.couriertracking.processor.config.HexagonConfig;
import io.kiyar.couriertracking.processor.model.CourierStoreEntrance;
import io.kiyar.couriertracking.processor.repository.CourierStoreEntranceRepository;
import io.kiyar.couriertracking.processor.strategy.DistanceCalculatorStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntranceService {

    private final CourierStoreEntranceRepository courierStoreEntranceRepository;
    private final DistanceCalculatorStrategy distanceCalculator;
    private final HexagonStoreCache storeCache;
    private final HexagonConfig hexagonConfig;
    private final EntranceConfig entranceConfig;
    private final H3Core h3Core;

    public void processEntrance(String courierId, Double lat, Double lng, Instant timestamp) {
        String h3Index = h3Core.geoToH3Address(lat, lng, hexagonConfig.getResolution());
        List<StoreCache> nearbyStores = storeCache.getStoresByHexagon(h3Index);

        log.info("Found {} stores for hexagon {} nearby", nearbyStores.size(), h3Index);

        for (StoreCache store : nearbyStores) {
            double distance = distanceCalculator.calculate(lat, lng, store.getLat(), store.getLng());

            log.info("Store lat: {}, lng: {}", store.getLat(), store.getLng());
            log.info("Calculated distance: {}", distance);

            if (distance > hexagonConfig.getMaxDistanceMeters()) {
                log.info("Skipping entrance for hexagon {} nearby", h3Index);
                continue;
            }

            boolean recentEntryExists = courierStoreEntranceRepository
                    .findFirstByCourierIdAndStoreIdOrderByEnteredAtDesc(courierId, store.getId())
                    .map(entry -> {
                        long seconds = Duration.between(entry.getEnteredAt(), timestamp).getSeconds();
                        return seconds < entranceConfig.getReentryThresholdSeconds();
                    })
                    .orElse(false);

            if (recentEntryExists) {
                log.info("Courier {} re-entered store {} and skipped", courierId, store.getName());
                continue;
            }

            CourierStoreEntrance entrance = new CourierStoreEntrance(courierId, store.getId(), timestamp);
            courierStoreEntranceRepository.save(entrance);

            log.info("Courier {} entered store {} at {}", courierId, store.getName(), timestamp);
        }
    }
}
