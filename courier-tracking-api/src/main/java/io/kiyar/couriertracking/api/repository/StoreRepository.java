package io.kiyar.couriertracking.api.repository;

import io.kiyar.couriertracking.api.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {

    Store findByEntityId(String entityId);
}
