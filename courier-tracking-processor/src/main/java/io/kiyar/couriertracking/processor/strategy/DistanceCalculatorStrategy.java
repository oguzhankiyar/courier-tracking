package io.kiyar.couriertracking.processor.strategy;

public interface DistanceCalculatorStrategy {

    double calculate(double lat1, double lng1, double lat2, double lng2);
}
