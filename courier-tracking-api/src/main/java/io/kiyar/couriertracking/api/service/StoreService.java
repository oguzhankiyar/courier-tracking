package io.kiyar.couriertracking.api.service;

import com.uber.h3core.H3Core;
import io.kiyar.couriertracking.api.cache.HexagonStoreCache;
import io.kiyar.couriertracking.api.cache.dto.StoreCache;
import io.kiyar.couriertracking.api.config.HexagonConfig;
import io.kiyar.couriertracking.api.dto.request.CreateStoreRequest;
import io.kiyar.couriertracking.api.dto.response.StoreResponse;
import io.kiyar.couriertracking.api.mapper.StoreMapper;
import io.kiyar.couriertracking.api.model.Store;
import io.kiyar.couriertracking.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final HexagonStoreCache storeCache;
    private final HexagonConfig hexagonConfig;
    private final H3Core h3Core;

    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(storeMapper::toResponse)
                .toList();
    }

    public StoreResponse createStore(CreateStoreRequest request) {
        Store store = new Store(request.getName(), request.getLat(), request.getLng());
        storeRepository.save(store);

        String centerIndex = h3Core.geoToH3Address(store.getLat(), store.getLng(), hexagonConfig.getResolution());
        storeCache.set(centerIndex, storeMapper.toCache(store));

        return storeMapper.toResponse(store);
    }

    public void refreshCache() {
        Map<String, List<StoreCache>> caches = new HashMap<>();

        for (Store store : storeRepository.findAll()) {
            String centerIndex = h3Core.geoToH3Address(store.getLat(), store.getLng(), hexagonConfig.getResolution());

            List<String> indexes = h3Core.kRing(centerIndex, hexagonConfig.getRingRadius());

            for (String index : indexes) {
                caches.computeIfAbsent(index, k -> new ArrayList<>())
                        .add(storeMapper.toCache(store));
            }
        }

        storeCache.setAll(caches);
    }
}
