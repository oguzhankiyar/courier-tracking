package io.kiyar.couriertracking.api.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "courier_store_entrances")
public class CourierStoreEntrance {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String entityId;

    private String courierId;
    private String storeId;
    private Instant enteredAt;

    private Instant createdAt;
    private Instant updatedAt;

    public CourierStoreEntrance() {
        this.entityId = UUID.randomUUID().toString();

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public CourierStoreEntrance(String courierId, String storeId, Instant enteredAt) {
        this();

        this.courierId = courierId;
        this.storeId = storeId;
        this.enteredAt = enteredAt;
    }
}
