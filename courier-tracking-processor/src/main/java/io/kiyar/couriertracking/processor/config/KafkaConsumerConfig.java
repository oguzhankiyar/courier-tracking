package io.kiyar.couriertracking.processor.config;

import io.kiyar.couriertracking.processor.event.CourierLocationUpdatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private ConsumerFactory<String, CourierLocationUpdatedEvent> createConsumerFactory(String groupSuffix) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "-" + groupSuffix);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<CourierLocationUpdatedEvent> deserializer =
                new JsonDeserializer<>(CourierLocationUpdatedEvent.class, false);
        deserializer.setRemoveTypeHeaders(true);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourierLocationUpdatedEvent> entranceKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, CourierLocationUpdatedEvent>();
        factory.setConsumerFactory(createConsumerFactory("entrance"));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourierLocationUpdatedEvent> distanceKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, CourierLocationUpdatedEvent>();
        factory.setConsumerFactory(createConsumerFactory("distance"));
        return factory;
    }
}

