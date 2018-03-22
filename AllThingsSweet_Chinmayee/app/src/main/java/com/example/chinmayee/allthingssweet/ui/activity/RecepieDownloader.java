package com.example.chinmayee.allthingssweet.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chinmayee.allthingssweet.IdlingResource.SimpleIdlingResource;
import com.example.chinmayee.allthingssweet.dto.RecepieAPIInterface;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.utils.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chinmayee on 3/17/18.
 */

public class RecepieDownloader {

    private static final int DELAY_MILLIS = 3000;
    static RecepieAPIInterface apiInterface;

    interface DelayerCallback{
        void onDone(List<RecepieList> recepies);
    }

    static void downloadRecepies(Context context, final DelayerCallback callback,
                              @Nullable final SimpleIdlingResource idlingResource) {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        apiInterface = APIClient.getClient().create(RecepieAPIInterface.class);
        Call<List<RecepieList>> call = apiInterface.getAllRecepies();
        call.enqueue(new Callback<List<RecepieList>>() {

            @Override
            public void onResponse(Call<List<RecepieList>> call, Response<List<RecepieList>> response) {
                Log.v("TAG",response.code()+"");
                final List<RecepieList> listRecpies=response.body();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onDone(listRecpies);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                            }
                        }
                    }
                }, DELAY_MILLIS);
            }

            @Override
            public void onFailure(Call<List<RecepieList>> call, Throwable t) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onDone(null);
                            if (idlingResource != null) {
                                idlingResource.setIdleState(true);
                            }
                        }
                    }
                }, DELAY_MILLIS);
            }
        });

    }

}
