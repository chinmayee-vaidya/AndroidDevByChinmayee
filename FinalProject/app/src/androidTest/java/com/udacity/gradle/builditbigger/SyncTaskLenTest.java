package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by Chinmayee on 5/17/18.
 */
@RunWith(AndroidJUnit4.class)
public class SyncTaskLenTest {



    @Test
    public void checkJokeLoad() throws InterruptedException {
            JokeFetchAsyncTask obj = new JokeFetchAsyncTask(false);
            obj.execute(InstrumentationRegistry.getContext());
        try {
            String joke = obj.get(5, TimeUnit.SECONDS);
            Assert.assertEquals(69,joke.length());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }




}
