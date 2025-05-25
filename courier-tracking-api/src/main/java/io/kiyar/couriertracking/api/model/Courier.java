package io.kiyar.couriertracking.api.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "couriers")
public class Courier {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String entityId;

    private String name;
    private double lastLocationLat;
    private double lastLocationLng;
    private double totalDistance;

    private Instant createdAt;
    private Instant updatedAt;

    public Courier() {
        this.entityId = UUID.randomUUID().toString();

        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public Courier(String name) {
        this();

        this.name = name;
    }
}
