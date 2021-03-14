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
        Assert.assertEquals((int)location1.getLatitude(), (int)location.getLongitude());
    }

    @Test
    public void changeLocation() {
        location.changeLocation(2, location.getLongitude());
        Assert.assertEquals((int)(location.getLatitude()), 2);
        Assert.assertEquals((int) location.getLongitude(), 1);
    }

    @Test
    public void getLatitude() {
        Assert.assertEquals((int)location.getLatitude(), 1);
    }

    @Test
    public void getLongitude() {
        Assert.assertEquals((int)location.getLongitude(), 1);
    }
}