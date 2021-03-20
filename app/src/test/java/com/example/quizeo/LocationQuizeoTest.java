package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocationQuizeoTest {
    private LocationQuizeo location;

    @Before
    public void init() {
        location = new LocationQuizeo(1, 1);
    }

    @Test
    public void initializationTest() {
        LocationQuizeo location1 = new LocationQuizeo(1, 1);
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