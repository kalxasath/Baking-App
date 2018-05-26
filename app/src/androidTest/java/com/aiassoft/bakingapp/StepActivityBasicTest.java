package com.aiassoft.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aiassoft.bakingapp.activities.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by gvryn on 26/05/18.
 */
@RunWith(AndroidJUnit4.class)
public class StepActivityBasicTest {
    @Rule public ActivityTestRule<StepActivity> mActivityTestRule
            = new ActivityTestRule<StepActivity>(StepActivity.class);

    @Test
    public void clickNextButton() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, StepActivity.class);
        intent.putExtra(Const.EXTRA_RECIPE_POS, 0);
        intent.putExtra(Const.EXTRA_STEP_POS, 0);

        mActivityTestRule.launchActivity(intent);

        // 1. Find the view
        // 2. Perform action on the view
        onView(withId(R.id.bt_next)).perform(click());

        // 3. Check if the view does what you expected
        onView(withId(R.id.bt_prev)).check((ViewAssertion) isDisplayed());


    }
}
