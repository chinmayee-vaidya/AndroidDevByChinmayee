package com.example.chinmayee.allthingssweet;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import com.example.chinmayee.allthingssweet.ui.activity.StepsActivity;
import com.example.chinmayee.allthingssweet.ui.activity.VideoActivity;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.example.chinmayee.allthingssweet.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Created by Chinmayee on 3/16/18.
 */

@RunWith(AndroidJUnit4.class)
public class RecepieToDescTest extends ActivityTestRule{

    private Context context;
    int screenWidth;
    @Rule
    public ActivityTestRule<StepsActivity> mTestActivityRule=new ActivityTestRule<StepsActivity>(StepsActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra(Constants.STEPS_ACTIVITY_KEY, TestUtils.getFakeRecepieData().get(0));
            return intent;
        }
    };
    private IdlingResource mIdlingResource;

    public RecepieToDescTest(){
        super(StepsActivity.class);
    }

    @Before
    public void registerIdlingResource() throws InterruptedException {

        getActivity();
        context= InstrumentationRegistry.getContext();
        screenWidth = dpToPx(context,
                context.getResources().getConfiguration().screenWidthDp);
        Thread.sleep(1000);
        mIdlingResource = mTestActivityRule.getActivity().getIdlingResource();



        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }
    @Test
    public void testView(){
        onView(withId(R.id.stepName))
                .check(matches(hasDescendant(withText("FAKE_SHORT_DESC1"))));
        onView(withId(R.id.stepName))
                .check(matches(hasDescendant(withText("FAKE_SHORT_DESC2"))));
        onView(withId(R.id.stepName))
                .check(matches(hasDescendant(withText("FAKE_SHORT_DESC3"))));
        onView(withId(R.id.stepName))
                .check(matches(hasDescendant(withText("FAKE_SHORT_DESC4"))));


    }

    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }




    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
