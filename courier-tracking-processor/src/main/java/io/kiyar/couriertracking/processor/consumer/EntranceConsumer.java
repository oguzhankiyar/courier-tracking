package io.kiyar.couriertracking.processor.consumer;

import io.kiyar.couriertracking.processor.event.CourierLocationUpdatedEvent;
import io.kiyar.couriertracking.processor.service.EntranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntranceConsumer {

    private final EntranceService entranceService;

    @KafkaListener(
            topics = "courier-location-updates",
            containerFactory = "entranceKafkaListenerContainerFactory"
    )
    public void consume(CourierLocationUpdatedEvent event) {
        log.info("EntranceConsumer received: {}", event);
        entranceService.processEntrance(
                event.getCourierId(),
                event.getLat(),
                event.getLng(),
                event.getTimestamp()
        );
    }
}
