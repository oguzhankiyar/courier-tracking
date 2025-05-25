package io.kiyar.couriertracking.processor.repository;


import io.kiyar.couriertracking.processor.model.Courier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends MongoRepository<Courier, String> {

    Optional<Courier> findByEntityId(String entityId);
}
