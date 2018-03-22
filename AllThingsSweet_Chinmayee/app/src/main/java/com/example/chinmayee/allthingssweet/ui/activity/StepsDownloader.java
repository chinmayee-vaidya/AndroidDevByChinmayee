package com.example.chinmayee.allthingssweet.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.chinmayee.allthingssweet.IdlingResource.SimpleIdlingResource;
import com.example.chinmayee.allthingssweet.dto.RecepieList;

import java.util.List;

/**
 * Created by Chinmayee on 3/17/18.
 */

public class StepsDownloader {
    private static final int DELAY_MILLIS = 3000;

    interface DelayerCallback{
        void onDone();
    }
    static void downloadRecepies(Context context, final StepsDownloader.DelayerCallback callback,
                                 @Nullable final SimpleIdlingResource idlingResource){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone();
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }

}
