package com.example.chinmayee.allthingssweet;

import android.content.res.Configuration;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.adapter.RecepiesAdapter;
import com.example.chinmayee.allthingssweet.ui.activity.StartBakingMain;
import static android.support.test.espresso.Espresso.onView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chinmayee on 3/13/18.
 */

@RunWith(AndroidJUnit4.class)
public class AllRecepies {

    public static Matcher<RecyclerView.ViewHolder> withItemSubject(final String subject) {
        Checks.checkNotNull(subject);
        return new BoundedMatcher<RecyclerView.ViewHolder,RecepiesAdapter.RecepieViewHolder>(
                RecepiesAdapter.RecepieViewHolder.class) {

            @Override
            protected boolean matchesSafely(RecepiesAdapter.RecepieViewHolder viewHolder) {
                TextView subjectTextView = (TextView)viewHolder.itemView.findViewById(R.id.recepie_name);

                return ((subject.equals(subjectTextView.getText().toString())
                        && (subjectTextView.getVisibility() == View.VISIBLE)));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item with subject: " + subject);
            }
        };
    }

    @Rule
    public ActivityTestRule<StartBakingMain> mTestActivityRule=new ActivityTestRule<StartBakingMain>(StartBakingMain.class);
    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mTestActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }


    @Test
    public void clickParticularItem(){
        onView(withId(R.id.recepieList))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recepieList))
                .check(matches(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.recepieList))
                .check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.recepieList))
                .check(matches(hasDescendant(withText("Cheesecake"))));
        onView(withId(R.id.recepieList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingnameview)).check(matches(withText("Ingredients")));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
