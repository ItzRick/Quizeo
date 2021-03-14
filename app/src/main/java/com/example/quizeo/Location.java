package com.example.quizeo;

public class Location {

    private double latitude;

    private double longitude;

    public Location(double x, double y) {
        latitude = x;
        longitude = y;
    }

    public void changeLocation(double x, double y) {
        latitude = x;
        longitude = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
