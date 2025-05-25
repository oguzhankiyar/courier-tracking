package io.kiyar.couriertracking.api.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kiyar.couriertracking.api.cache.dto.StoreCache;
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

    public void set(String key, StoreCache value) {
        String cacheKey = PREFIX + key + SUFFIX;
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(cacheKey, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize store list for key: {}", key, e);
        }
    }

    public void setAll(Map<String, List<StoreCache>> map) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, List<StoreCache>> entry : map.entrySet()) {
            String key = PREFIX + entry.getKey() + SUFFIX;
            try {
                String json = objectMapper.writeValueAsString(entry.getValue());
                newMap.put(key, json);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize store list for key: {}", key, e);
            }
        }
        redisTemplate.opsForValue().multiSet(newMap);
    }
}
