package com.softwarica.printstation.ui.auth;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.softwarica.printstation.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {
        ViewInteraction edtEmail = onView(allOf(withId(R.id.email), isDisplayed()));
        ViewInteraction edtPassword = onView(allOf(withId(R.id.password), isDisplayed()));
        ViewInteraction loginButton = onView(allOf(isDisplayed(), withId(R.id.loginButton)));


        edtEmail.perform(replaceText("suman@gmail.com"), closeSoftKeyboard());
        edtPassword.perform(replaceText("suman143"), closeSoftKeyboard());

        loginButton.perform(click());
    }
}

