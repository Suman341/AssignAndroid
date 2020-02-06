package com.softwarica.printstation.dashboard;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.softwarica.printstation.R;
import com.softwarica.printstation.ui.dashboard.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {
    @Rule
    public ActivityTestRule<DashboardActivity> mActivityTestRule = new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void loginActivityTest() {

        ViewInteraction aboutUsNavigationBUtton = onView(allOf(isDisplayed(), withId(R.id.nav_about_us)));

        aboutUsNavigationBUtton.perform(click());
    }

    @Test
    public void cartActivityTest() {

        ViewInteraction cartIntercation = onView(allOf(isDisplayed(), withId(R.id.cart)));
        cartIntercation.perform(click());
    }
}