package io.kiyar.couriertracking.processor.consumer;

import io.kiyar.couriertracking.processor.event.CourierLocationUpdatedEvent;
import io.kiyar.couriertracking.processor.service.DistanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DistanceConsumer {

    private final DistanceService distanceService;

    @KafkaListener(
            topics = "courier-location-updates",
            containerFactory = "distanceKafkaListenerContainerFactory"
    )
    public void consume(CourierLocationUpdatedEvent event) {
        log.info("DistanceConsumer received: {}", event);
        distanceService.processLocationUpdate(
                event.getCourierId(),
                event.getLat(),
                event.getLng(),
                event.getTimestamp()
        );
    }
}