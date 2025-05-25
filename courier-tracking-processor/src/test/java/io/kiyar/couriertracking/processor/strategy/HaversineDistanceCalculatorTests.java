package io.kiyar.couriertracking.processor.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineDistanceCalculatorTests {

    private final HaversineDistanceCalculator calculator = new HaversineDistanceCalculator();

    @Test
    void shouldReturnZeroForSameLocation() {
        double lat = 40.0;
        double lng = 29.0;

        double distance = calculator.calculate(lat, lng, lat, lng);

        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void shouldReturnApproxCorrectDistanceBetweenTwoPoints() {
        double distance = calculator.calculate(40.9896, 29.0283, 41.0558, 29.0210);

        assertTrue(distance > 7000 && distance < 7700, "Distance should be around 7350 meters");
    }

    @Test
    void shouldBeSymmetric() {
        double d1 = calculator.calculate(40.0, 29.0, 41.0, 30.0);
        double d2 = calculator.calculate(41.0, 30.0, 40.0, 29.0);

        assertEquals(d1, d2, 0.0001, "Haversine must be symmetric");
    }
}
