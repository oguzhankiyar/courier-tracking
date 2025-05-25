FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /build/target/courier-tracking-api.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]