package com.example.test.earthquake;

import android.app.Application;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest extends ApplicationTestCase<Application> {

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule(MapsActivity.class);

    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void markersShown() {

    }
}