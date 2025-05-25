package io.kiyar.couriertracking.processor.repository;

import io.kiyar.couriertracking.processor.model.CourierStoreEntrance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierStoreEntranceRepository extends MongoRepository<CourierStoreEntrance, String> {

    List<CourierStoreEntrance> findByCourierId(String courierId);

    Optional<CourierStoreEntrance> findFirstByCourierIdAndStoreIdOrderByEnteredAtDesc(String courierId, String storeId);
}
