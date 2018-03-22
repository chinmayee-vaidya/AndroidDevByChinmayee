package com.example.chinmayee.allthingssweet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Parcelable;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.chinmayee.allthingssweet.ui.activity.StartBakingMain;
import com.example.chinmayee.allthingssweet.ui.activity.StepsActivity;
import com.example.chinmayee.allthingssweet.ui.activity.VideoActivity;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.example.chinmayee.allthingssweet.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.chinmayee.allthingssweet.OrientationChangeAction.orientationPortrait;

/**
 * Created by Chinmayee on 3/13/18.
 */

@RunWith(AndroidJUnit4.class)
public class VideoTest extends ActivityTestRule{

    private Context context;
    private boolean mIsScreenSw600dp;

    @Rule
    public ActivityTestRule<VideoActivity> mTestActivityRule=new ActivityTestRule<VideoActivity>(VideoActivity.class,true,false){
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constants.VIDEO_KEY,(ArrayList<? extends Parcelable>) TestUtils.getFakeStepData());
            intent.putExtra(Constants.REM_VIDS, 0);
            intent.putExtra(Constants.STEPS_ACTIVITY_KEY,TestUtils.getFakeRecepieData().get(0));
            return intent;
        }
    };

    public VideoTest(){
        super(VideoActivity.class);
    }


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() throws InterruptedException {
        try{
        getActivity();
        context=InstrumentationRegistry.getContext();
        //mIsScreenSw600dp=context.getResources().getBoolean(R.bool.isTablet);
        System.out.println("The value is: "+mIsScreenSw600dp);
        onView(isRoot()).perform(orientationPortrait());
        Thread.sleep(1000);

        Log.v("WIDTH==", mIsScreenSw600dp+"");
        mIdlingResource = mTestActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);}catch(Exception e){

        }

    }


    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    @Test
    public void checkTests(){
        int screenWidth = dpToPx(context,
                context.getResources().getConfiguration().screenWidthDp);
        if(screenWidth<600){
            getActivity();
            onView(withId(R.id.videoDesc)).check(matches(withText("FAKE_DESC1")));
            onView((withId(R.id.nextButton)))
                    .perform(click());
            onView(withId(R.id.videoDesc)).check(matches(withText("FAKE_DESC2")));
            onView((withId(R.id.previousButton)))
                    .perform(click());
            onView(withId(R.id.videoDesc)).check(matches(withText("FAKE_DESC1")));
        }
    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
