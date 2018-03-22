package com.example.chinmayee.allthingssweet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.IdlingResource.SimpleIdlingResource;
import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.adapter.RecepiesAdapter;
import com.example.chinmayee.allthingssweet.data.RecepieContract;
import com.example.chinmayee.allthingssweet.dto.RecepieAPIInterface;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.utils.APIClient;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.example.chinmayee.allthingssweet.utils.JSONUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chinmayee.allthingssweet.utils.Constants.STEPS_ACTIVITY_KEY;

public class StartBakingMain extends AppCompatActivity implements RecepiesAdapter.RecepieOnClickListener, RecepieDownloader.DelayerCallback{
    RecepiesAdapter recAdapter;

    RecepieAPIInterface apiInterface;
    @BindView(R.id.recepieList) RecyclerView recView;
    @BindView(R.id.tv_error_message_display) TextView errMsg;
    @BindView(R.id.pb_loading_indicator)ProgressBar progBar;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    public boolean isTablet(){
        boolean bool = getResources().getBoolean(R.bool.isTablet);
        return bool;
    }

    public void showRecyclerView(){
        recView.setVisibility(View.VISIBLE);
        errMsg.setVisibility(View.INVISIBLE);
    }

    public void showErrorView(){
        errMsg.setVisibility(View.VISIBLE);
        recView.setVisibility(View.INVISIBLE);
    }

    public int calcItemsInGrid(){
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_baking_main);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(RecepieAPIInterface.class);
        if(isTablet()){
            GridLayoutManager layoutManager=new GridLayoutManager(this, calcItemsInGrid());
            recView.setLayoutManager(layoutManager);
        }
        else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            recView.setLayoutManager(layoutManager);
        }

        recAdapter = new RecepiesAdapter(this,this);
        recView.setAdapter(recAdapter);
        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecepieDownloader.downloadRecepies(this, StartBakingMain.this, mIdlingResource);
    }

    @Override
    public void onClick(RecepieList recepieDet) {
        Context context = this;
        Class destinationClass = StepsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(STEPS_ACTIVITY_KEY,recepieDet);
        intentToStartDetailActivity.putExtra(Constants.VID_POS,0);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onDone(List<RecepieList> recepies) {
        //getApplicationContext().getContentResolver().delete(RecepieContract.RecepiesEntry.CONTENT_URI,null,null);
        if(recepies==null || recepies.size()==0){
            showErrorView();
        }
        else{
            showRecyclerView();
            JSONUtils.insertIntoDB(getApplicationContext(),recepies, RecepieContract.RecepiesEntry.CONTENT_URI);
            recAdapter.swapAdapter(recepies);
        }

    }
}
