package io.kiyar.couriertracking.api.repository;

import io.kiyar.couriertracking.api.model.CourierStoreEntrance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierStoreEntranceRepository extends MongoRepository<CourierStoreEntrance, String> {

    List<CourierStoreEntrance> findByCourierId(String courierId);
}
