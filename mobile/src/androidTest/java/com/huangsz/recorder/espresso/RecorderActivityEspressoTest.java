package com.huangsz.recorder.espresso;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.huangsz.recorder.RecorderActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
public class RecorderActivityEspressoTest extends ActivityInstrumentationTestCase2<RecorderActivity> {

    public RecorderActivityEspressoTest() {
        super(RecorderActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testListGoesOverTheFold() {
        onView(withText("Add")).check(matches(isDisplayed()));
    }
}