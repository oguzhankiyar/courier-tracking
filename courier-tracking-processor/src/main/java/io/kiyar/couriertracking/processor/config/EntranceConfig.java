package io.kiyar.couriertracking.processor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "entrance")
public class EntranceConfig {

    private long reentryThresholdSeconds;
}
