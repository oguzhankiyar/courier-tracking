package io.kiyar.couriertracking.processor.config;

import com.uber.h3core.H3Core;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Data
@Configuration
@ConfigurationProperties(prefix = "hexagon")
public class HexagonConfig {

    private int resolution;
    private int ringRadius;
    private int maxDistanceMeters;

    @Bean
    public H3Core h3Core() throws IOException {
        return H3Core.newInstance();
    }
}