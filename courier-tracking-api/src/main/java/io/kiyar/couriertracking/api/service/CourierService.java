package io.kiyar.couriertracking.api.service;

import io.kiyar.couriertracking.api.dto.request.CreateCourierRequest;
import io.kiyar.couriertracking.api.dto.response.CourierResponse;
import io.kiyar.couriertracking.api.dto.response.CourierStoreEntranceResponse;
import io.kiyar.couriertracking.api.mapper.CourierMapper;
import io.kiyar.couriertracking.api.model.Courier;
import io.kiyar.couriertracking.api.repository.CourierRepository;
import io.kiyar.couriertracking.api.repository.CourierStoreEntranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final CourierStoreEntranceRepository courierStoreEntranceRepository;
    private final CourierMapper courierMapper;

    public List<CourierResponse> getAllCouriers() {
        return courierRepository.findAll()
                .stream()
                .map(courierMapper::toResponse)
                .toList();
    }

    public Optional<CourierResponse> getCourierById(String courierId) {
        return courierRepository
                .findByEntityId(courierId)
                .map(courierMapper::toResponse);
    }

    public List<CourierStoreEntranceResponse> getEntrancesByCourierId(String courierId) {
        return courierStoreEntranceRepository
                .findByCourierId(courierId)
                .stream()
                .map(courierMapper::toStoreEntranceResponse)
                .toList();
    }

    public CourierResponse createCourier(CreateCourierRequest request) {
        Courier courier = new Courier(request.getName());
        courierRepository.save(courier);
        return courierMapper.toResponse(courier);
    }
}
