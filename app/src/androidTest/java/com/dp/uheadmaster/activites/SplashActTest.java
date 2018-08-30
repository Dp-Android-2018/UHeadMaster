package com.dp.uheadmaster.activites;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashActTest {

    @Rule
    public ActivityTestRule<SplashAct> mActivityTestRule = new ActivityTestRule<>(SplashAct.class);

    @Test
    public void splashActTest() {
    }

}
