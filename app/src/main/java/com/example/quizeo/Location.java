package com.example.quizeo;

public class Location {

    private float x;

    private float y;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void changeLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
