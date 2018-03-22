package com.example.chinmayee.allthingssweet.ui.activity;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.IdlingResource.SimpleIdlingResource;
import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.adapter.FragmentSectionsAdapter;
import com.example.chinmayee.allthingssweet.adapter.StepsAdapter;
import com.example.chinmayee.allthingssweet.data.RecepieContract;
import com.example.chinmayee.allthingssweet.dto.Ingredient;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.dto.Step;
import com.example.chinmayee.allthingssweet.ui.fragment.IngredientListFragment;
import com.example.chinmayee.allthingssweet.ui.fragment.StepListFragment;
import com.example.chinmayee.allthingssweet.ui.fragment.VideoFragment;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.example.chinmayee.allthingssweet.utils.JSONUtils;
import com.example.chinmayee.allthingssweet.widget.IngredientWidgetService;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.IS_WID;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.RECEPIE_ID;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.RECEPIE_NAME;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.REC_IMAGE;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.REC_SERV_SIZE;
import static com.example.chinmayee.allthingssweet.utils.Constants.ADD_WIDGET;
import static com.example.chinmayee.allthingssweet.utils.Constants.NO_ANS;
import static com.example.chinmayee.allthingssweet.utils.Constants.PLAY_WHEN_READY_VAL;
import static com.example.chinmayee.allthingssweet.utils.Constants.REMOVE_WIDGET;
import static com.example.chinmayee.allthingssweet.utils.Constants.VID_SECS;
import static com.example.chinmayee.allthingssweet.utils.Constants.YES_ANS;
import static com.example.chinmayee.allthingssweet.widget.IngredientWidgetService.UPDATE_WIDGET;

public class StepsActivity extends AppCompatActivity  implements StepsAdapter.StepOnClickListener, StepsDownloader.DelayerCallback{

    RecepieList recObj;
    int stepIndex;
    long playback = 0;
    boolean playWhenReady = true;

    @Nullable
    @BindView(R.id.descTabTv) TextView descTextView;
    VideoFragment videoFragment;

    public boolean checkIfTab(){
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        return tabletSize;

    }


    FragmentSectionsAdapter tabsAdapter;
    IngredientListFragment ingDetails;
    StepListFragment stepDetails;
    private ViewPager mViewpager;
    public boolean isSavedInstanceState=false;
    public static final String[] REC_COLUMNS={RECEPIE_ID,RECEPIE_NAME,REC_SERV_SIZE,REC_IMAGE,IS_WID};


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



    @Override
    protected void onStop() {
        super.onStop();
        isSavedInstanceState=true;
        if(videoFragment!=null){
            String  stepUrl = videoFragment.getAllSteps().get(videoFragment.getStepIndex()).getVideoURL();
            if(stepUrl==null || stepUrl.equals("")){
                playWhenReady=true;
                playback=0;
            }
            else{
                playWhenReady=videoFragment.retuenValuePlayWhenReady();
                playback=videoFragment.getPlaybackPosition();
                videoFragment.releasePlayer();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(recObj.getName());
        invalidateOptionsMenu();

        if(findViewById(R.id.linearLayoutLand)!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            Bundle bundleSteps = new Bundle();
            List<Step> stepList=recObj.getSteps();
            List<Ingredient> ingList = recObj.getIngredients();
            bundle.putParcelable(Constants.INGREDIENTS_KEY, recObj);
            bundle.putParcelable(Constants.STEPS_KEY, recObj);
            ingDetails = new IngredientListFragment();
            stepDetails = new StepListFragment();
            stepDetails.setPos(stepIndex);
            ingDetails.setArguments(bundle);
            stepDetails.setArguments(bundle);
            ingDetails.setRetainInstance(true);
            stepDetails.setRetainInstance(true);
            videoFragment = new VideoFragment();
            videoFragment.setPosition(playback);
            videoFragment.setPlayWhenReady(playWhenReady);
            ArrayList<Step> arr = new ArrayList<>();
            for(Step stp: stepList){
                arr.add(stp);
            }
            descTextView.setText(stepList.get(stepIndex).getDescription());
            videoFragment.setAllSteps(arr);
            videoFragment.setStepIndex(stepIndex);



            if(isSavedInstanceState){
                fragmentManager.beginTransaction()
                        .replace(R.id.ing_container,ingDetails)
                        .commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.step_container,stepDetails)
                        .commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.video_container, videoFragment)
                        .commit();
            }
            else{
                fragmentManager.beginTransaction()
                        .add(R.id.ing_container,ingDetails)
                        .commit();
                fragmentManager.beginTransaction()
                        .add(R.id.step_container,stepDetails)
                        .commit();
                fragmentManager.beginTransaction()
                        .add(R.id.video_container, videoFragment)
                        .commit();
            }



        }
        else{

            mViewpager=(ViewPager)findViewById(R.id.container);
            tabsAdapter=new FragmentSectionsAdapter(getSupportFragmentManager());
            Bundle bundle = new Bundle();

            Bundle bundleSteps = new Bundle();
            List<Step> stepList=recObj.getSteps();
            List<Ingredient> ingList = recObj.getIngredients();
            bundle.putParcelable(Constants.INGREDIENTS_KEY, recObj);
            bundle.putParcelable(Constants.STEPS_KEY, recObj);
            ingDetails = new IngredientListFragment();
            stepDetails = new StepListFragment();
            stepDetails.setPos(stepIndex);
            ingDetails.setArguments(bundle);
            stepDetails.setArguments(bundle);
            ingDetails.setRetainInstance(true);
            stepDetails.setRetainInstance(true);
            tabsAdapter.addFragment(ingDetails,Constants.ING_TAB_TITLE);
            tabsAdapter.addFragment(stepDetails,Constants.STEP_TAB_TITLE);
            mViewpager.setAdapter(tabsAdapter);
            TabLayout tabLayout =(TabLayout)findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewpager);
        }
    }

    public ArrayList<Step> getStepArryList(List<Step> list){
        ArrayList<Step> arr = new ArrayList<>();
        for(Step step: list){
            arr.add(step);
        }
        return arr;
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.REC_LIST_OBJECT,recObj);
        outState.putInt(Constants.VID_POS,stepIndex);
        if(videoFragment!=null) {
            String  stepUrl = videoFragment.getAllSteps().get(videoFragment.getStepIndex()).getVideoURL();
            if(stepUrl==null || stepUrl.equals("")){
                outState.putLong(Constants.VID_SECS, 0);
                outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,true);
            }
            else{
                outState.putLong(Constants.VID_SECS, videoFragment.getPlaybackPosition());
                outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,videoFragment.retuenValuePlayWhenReady());
            }

        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            recObj=savedInstanceState.getParcelable(Constants.REC_LIST_OBJECT);
            stepIndex=savedInstanceState.getInt(Constants.VID_POS);
            playback=savedInstanceState.getLong(Constants.VID_SECS);
            playWhenReady=savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY_VAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        if(savedInstanceState!=null){
            recObj=savedInstanceState.getParcelable(Constants.REC_LIST_OBJECT);
            stepIndex= savedInstanceState.getInt(Constants.VID_POS);
            playback=savedInstanceState.getLong(VID_SECS);
            playWhenReady=savedInstanceState.getBoolean(PLAY_WHEN_READY_VAL);
            isSavedInstanceState=true;
        }
        else {
            Intent intentThatStartedThisActivity = getIntent();
            if (intentThatStartedThisActivity != null) {
                recObj = intentThatStartedThisActivity.getExtras().getParcelable(Constants.STEPS_ACTIVITY_KEY);
                stepIndex=intentThatStartedThisActivity.getExtras().getInt(Constants.VIDEO_KEY);
            }
            isSavedInstanceState=false;
        }


        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        StepsDownloader.downloadRecepies(this, StepsActivity.this, mIdlingResource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addtomenubutton,menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Cursor cursor = getApplicationContext().getContentResolver().query(RecepieContract.RecepiesEntry.buildRecUriWithID(recObj.getId()+""),REC_COLUMNS,null,null,null);
        String isfav="";
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                isfav = cursor.getString(cursor.getColumnIndex(IS_WID));
                // do what ever you want here
                cursor.moveToNext();
            }
        }
        cursor.close();
        if(isfav.equals(NO_ANS)){
            menu.findItem(R.id.widgetbutton).setTitle(ADD_WIDGET);
        }
        else{
            menu.findItem(R.id.widgetbutton).setTitle(REMOVE_WIDGET);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Uri uri = RecepieContract.RecepiesEntry.buildRecUriWithID(recObj.getId()+"");
        if(id==android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.widgetbutton){
            String title = item.getTitle().toString();
            if(title.equals(ADD_WIDGET)){
                ContentValues contentVals = JSONUtils.getValues(recObj, YES_ANS);
                getApplicationContext().getContentResolver().update(uri,contentVals,null,null);
                item.setTitle(REMOVE_WIDGET);
                Intent intent = new Intent(this, IngredientWidgetService.class);
                intent.setAction(UPDATE_WIDGET);
                this.startService(intent);
            }
            else{
                ContentValues contentVals = JSONUtils.getValues(recObj, NO_ANS);
                getApplicationContext().getContentResolver().update(uri,contentVals,null,null);
                item.setTitle(ADD_WIDGET);
                Intent intent = new Intent(this, IngredientWidgetService.class);
                intent.setAction(UPDATE_WIDGET);
                this.startService(intent);
            }

        }
        return true;
    }

    @Override
    public void onClick(ArrayList<Step> step, RecepieList obj, int pos) {
        if(findViewById(R.id.linearLayoutLand)==null){
            Intent nextIntent = new Intent(this, VideoActivity.class);
            nextIntent.putParcelableArrayListExtra(Constants.VIDEO_KEY,step);
            nextIntent.putExtra(Constants.VIDEO_INDEX,pos);
            nextIntent.putExtra(Constants.STEPS_ACTIVITY_KEY,obj);
            startActivity(nextIntent);
        }
        else{
            videoFragment = new VideoFragment();
            stepIndex=pos;
            videoFragment.setAllSteps(step);
            videoFragment.setStepIndex(pos);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit();
            descTextView.setText(step.get(pos).getDescription());
        }

    }

    @Override
    public void onDone() {

    }
}