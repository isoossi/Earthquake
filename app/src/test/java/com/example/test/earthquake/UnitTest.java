package com.example.test.earthquake;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mikkoastrom on 1/19/18.
 */

public class UnitTest {

    @Test
    public void serialize_deserialize() throws Exception {
        MapsActivity distance = new MapsActivity();
        double test = distance.distance(60.1699,49.2827, 24.9384, 123.1207);
        System.out.println(test);
        Assert.assertEquals(6735,test,50.0);
    }
}
