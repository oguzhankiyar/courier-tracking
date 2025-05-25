package io.kiyar.couriertracking.processor.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kiyar.couriertracking.processor.cache.dto.StoreCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class HexagonStoreCache {

    private static final String PREFIX = "hexagons:";
    private static final String SUFFIX = ":stores";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<StoreCache> getStoresByHexagon(String hexagon) {
        String key = PREFIX + hexagon + SUFFIX;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<StoreCache>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize store list for key: {}", key, e);
            return Collections.emptyList();
        }
    }
}
