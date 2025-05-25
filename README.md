# Courier Tracking

This project is a microservice-based real-time courier tracking system.
It ingests courier geolocation data, detects store entrances based on proximity, and calculates total travel distances.

- Uses [H3](https://h3geo.org/) for indexing store coordinates
- Entry is detected if courier is within 100 meters of a store
- Re-entry within 60 seconds is ignored

## Structure

### **`courier-tracking-api`**
- Exposes REST endpoints
- Receives courier location updates
- Validates timestamp
- Publishes events to Kafka

### **`courier-tracking-processor`**
- Listens to Kafka events and performs distance calculation
- Store entrance detection using H3 hexagonal spatial indexing

## Stack

- Java 17
- Spring Boot 3.x
- Apache Kafka
- MongoDB
- Redis
- Uber H3 (Geospatial indexing)
- Docker & Docker Compose
- JUnit 5 + Mockito

## Running

### 1. Build the Services via Docker (Maven inside)

```bash
docker-compose up --build
```

This will:
- Build `courier-tracking-api` and `courier-tracking-processor` using multi-stage Dockerfiles
- Start supporting services (MongoDB, Redis, Kafka, Zookeeper)

### 2. Access the API

```bash
open http://localhost:2801/swagger
```

```bash
open http://localhost:2802/swagger
```

### 3. Create initial data

#### Create courier
```bash
curl -X 'POST' \
  'http://localhost:2801/couriers' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Oguzhan"
}'
```

#### Create store
```bash
curl -X 'POST' \
  'http://localhost:2801/stores' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Metropol",
  "lat": 40.99401536081996,
  "lng": 29.122665069002927
}'
```

#### Refresh store cache
```bash
curl -X 'POST' \
  'http://localhost:2801/stores/cache/refresh' \
  -H 'accept: */*' \
  -d ''
```

### 4. Make the update

#### Send location update
```bash
curl -X 'POST' \
  'http://localhost:2801/locations' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "courierId": "eef71917-807e-4a37-b480-eb0f2395061f",
  "lat": 40.993979436344986,
  "lng": 29.122635307765798,
  "timestamp": "2025-05-25T16:00:54.454Z"
}'
```

### 5. Check the data

#### Check couriers
```bash
curl -X 'GET' \
  'http://localhost:2801/couriers' \
  -H 'accept: */*'
```

#### Check courier by id
```bash
curl -X 'GET' \
  'http://localhost:2801/couriers/eef71917-807e-4a37-b480-eb0f2395061f' \
  -H 'accept: */*'
```

#### Check courier entrances
```bash
curl -X 'GET' \
  'http://localhost:2801/couriers/eef71917-807e-4a37-b480-eb0f2395061f/entrances' \
  -H 'accept: */*'
```

## Configuration

Defined in `application.yml` or overridden via environment variables:

```yaml
location:
  timestamp-tolerance-seconds: 60

hexagon:
  resolution: 9
  ring-radius: 1
  max-distance-meters: 100

entrance:
  reentry-threshold-seconds: 60
```

## Happy Coding!
