package io.kiyar.couriertracking.processor.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "stores")
public class Store {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String entityId;

    private String name;
    private double lat;
    private double lng;

    private Instant createdAt;
    private Instant updatedAt;

    public Store() {
        this.entityId = UUID.randomUUID().toString();

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public Store(String name, Double lat, Double lng) {
        this();

        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}
