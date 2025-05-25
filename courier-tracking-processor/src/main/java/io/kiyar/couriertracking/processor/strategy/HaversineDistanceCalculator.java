package io.kiyar.couriertracking.processor.strategy;

import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceCalculator implements DistanceCalculatorStrategy {

    private static final int EARTH_RADIUS_IN_METERS = 6371000;

    @Override
    public double calculate(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_IN_METERS * c;
    }
}