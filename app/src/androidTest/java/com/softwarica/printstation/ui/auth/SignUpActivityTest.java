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
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {
    @Rule
    public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void signUpActivityTest() {
        ViewInteraction edtfirstname = onView(allOf(withId(R.id.firstName), isDisplayed()));
        ViewInteraction edtlastname = onView(allOf(withId(R.id.lastName), isDisplayed()));
        ViewInteraction edtaddress = onView(allOf(withId(R.id.address), isDisplayed()));
        ViewInteraction edtphone = onView(allOf(withId(R.id.phone), isDisplayed()));
        ViewInteraction edtEmail = onView(allOf(withId(R.id.email), isDisplayed()));
//        ViewInteraction edtPassword = onView(allOf(withId(R.id.password), isDisplayed()));
        ViewInteraction signupButton = onView(allOf(isDisplayed(), withId(R.id.btnSignUp)));


        edtfirstname.perform(replaceText("Saroj"), closeSoftKeyboard());
        edtfirstname.perform(replaceText("shrestha"), closeSoftKeyboard());
        edtaddress.perform(replaceText("ktm"), closeSoftKeyboard());
        edtEmail.perform(replaceText("shrestha@gmail.com"), closeSoftKeyboard());
        edtphone.perform(replaceText("98415847582"), closeSoftKeyboard());


        signupButton.perform(click());
    }
}