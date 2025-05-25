package io.kiyar.couriertracking.processor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.application")
public class AppProperties {

    private String name;
    private String version;
    private String environment;
}