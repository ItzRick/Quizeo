package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {
    private Location location;

    @Before
    public void init() {
        location = new Location(1, 1);
    }

    @Test
    public void initializationTest() {
        Location location1 = new Location (1, 1);
        Assert.assertEquals((int)location1.getX(), (int)location.getX());
    }

    @Test
    public void changeLocation() {
        location.changeLocation(2, location.getY());
        Assert.assertEquals((int)(location.getX()), 2);
        Assert.assertEquals((int) location.getY(), 1);
    }

    @Test
    public void getX() {
        Assert.assertEquals((int)location.getX(), 1);
    }

    @Test
    public void getY() {
        Assert.assertEquals((int)location.getY(), 1);
    }
}