package com.example.chinmayee.allthingssweet.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.chinmayee.allthingssweet.IdlingResource.SimpleIdlingResource;
import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.dto.Step;
import com.example.chinmayee.allthingssweet.ui.fragment.PaginationFragment;
import com.example.chinmayee.allthingssweet.ui.fragment.VideoFragment;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity implements StepsDownloader.DelayerCallback{
    static ArrayList<Step> stepArr;
    int videoPos;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();
    RecepieList recObj;
    public static long playBackPos=0;
    public VideoFragment videoFragment;


    @Nullable
    @BindView(R.id.recVideoViewLand)

    SimpleExoPlayerView mPlayerView;
    @Nullable
    @BindView(R.id.videoDesc)  TextView tvDesc;


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

    //Variables for landscape

    SimpleExoPlayer player;

    boolean playWhenReady=true;
    int currentWindow = 0;
    long playbackPosition =0;
    MediaSource mediaSource;


    public boolean isTablet(){
        boolean bool = getResources().getBoolean(R.bool.isTablet);
        return bool;
    }

    public boolean isDeviceLandscape(){
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT && !isTablet())
            return false;
        else return true;
    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(Constants.VIDEO_CLASS),
        new DefaultExtractorsFactory(), null, null);
    }

    /**@Override
    protected void onPause() {
        if(isDeviceLandscape()){

        }
        super.onPause();
    }**/

    @Override
    protected void onStop() {
        super.onStop();

        if(!isDeviceLandscape()){
            if(videoFragment!=null){
                String  stepUrl = videoFragment.getAllSteps().get(videoFragment.getStepIndex()).getVideoURL();
                if(stepUrl==null || stepUrl.equals("")){
                    playWhenReady=true;
                    playbackPosition=0;
                }
                else{
                    playWhenReady=videoFragment.retuenValuePlayWhenReady();
                    playbackPosition=videoFragment.getPlaybackPosition();
                    videoFragment.releasePlayer();
                }


            }
            releasePlayer();

        }
        else{
            if(player!=null){
                playWhenReady=player.getPlayWhenReady();
                playbackPosition=player.getCurrentPosition();
                releasePlayer();
            }
            releasePlayer();
        }

    }

    @Override
    protected void onResume() {
        if (isDeviceLandscape()) {
            if(!stepArr.get(videoPos).getVideoURL().equals("")){
                Uri uri = Uri.parse(stepArr.get(videoPos).getVideoURL());
                TrackSelection.Factory adaptiveTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(this),
                        new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
                mPlayerView.setPlayer(player);
                player.setPlayWhenReady(playWhenReady);
                player.seekTo(currentWindow, playbackPosition);
                mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource, true, false);
            }

        } else {
            if (!stepArr.get(videoPos).getVideoURL().equals("")) {
                FrameLayout frameLayout = findViewById(R.id.video_container);
                frameLayout.setVisibility(View.VISIBLE);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            videoFragment = new VideoFragment();
            videoFragment.setAllSteps(stepArr);
            videoFragment.setStepIndex(videoPos);
            videoFragment.setPosition(playbackPosition);
            videoFragment.setPlayWhenReady(playWhenReady);

            fragmentManager.beginTransaction()
                    .add(R.id.video_container, videoFragment)
                    .commit();


            tvDesc.setText(stepArr.get(videoPos).getDescription());

            PaginationFragment pageFragment = new PaginationFragment();
            pageFragment.setCurrentPage(videoPos);
            pageFragment.setSteps(stepArr);
            fragmentManager.beginTransaction()
                    .add(R.id.pageinationContainer, pageFragment)
                    .commit();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        boolean newActivity = ! getIntent().hasExtra(Constants.REM_VIDS);
        if(savedInstanceState!=null){
            stepArr = savedInstanceState.getParcelableArrayList(Constants.VIDEO_KEY);
            videoPos=savedInstanceState.getInt(Constants.VIDEO_INDEX);
            recObj = savedInstanceState.getParcelable(Constants.STEPS_ACTIVITY_KEY);
            playbackPosition=savedInstanceState.getLong(Constants.VID_SECS);
            playWhenReady=savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY_VAL);
        }
        else{
            playbackPosition=0;
            if(newActivity){
                Intent intentThatStartedThisActivity = getIntent();
                if(intentThatStartedThisActivity !=null){
                    stepArr = intentThatStartedThisActivity.getExtras().getParcelableArrayList(Constants.VIDEO_KEY);
                    videoPos=intentThatStartedThisActivity.getExtras().getInt(Constants.VIDEO_INDEX);
                    recObj = intentThatStartedThisActivity.getExtras().getParcelable(Constants.STEPS_ACTIVITY_KEY);
                }

            }
            else{
                Intent intentThatStartedThisActivity = getIntent();
                if(intentThatStartedThisActivity !=null){
                    stepArr = intentThatStartedThisActivity.getExtras().getParcelableArrayList(Constants.VIDEO_KEY);
                    videoPos=intentThatStartedThisActivity.getExtras().getInt(Constants.REM_VIDS);
                    recObj = intentThatStartedThisActivity.getExtras().getParcelable(Constants.STEPS_ACTIVITY_KEY);
                }
            }
        }

        getSupportActionBar().setTitle(recObj.getName());
        getIdlingResource();


    }


    @Override
    public void onBackPressed(){
        Intent i = new Intent(VideoActivity.this,StepsActivity.class);
        i.putExtra(Constants.STEPS_ACTIVITY_KEY,recObj);
        i.putExtra(Constants.VIDEO_KEY,videoPos);
        startActivity(i);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.VIDEO_KEY,stepArr);
        outState.putInt(Constants.VIDEO_INDEX,videoPos);
        outState.putParcelable(Constants.STEPS_ACTIVITY_KEY,recObj);
        if(videoFragment!=null){
            String  stepUrl = videoFragment.getAllSteps().get(videoFragment.getStepIndex()).getVideoURL();
            if(stepUrl==null || stepUrl.equals("")){
                outState.putLong(Constants.VID_SECS,0);
                outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,true);
            }
            else{
                outState.putLong(Constants.VID_SECS,videoFragment.getPlaybackPosition());
                outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,videoFragment.retuenValuePlayWhenReady());

            }
        }

        else if(player!=null)
        {
            outState.putLong(Constants.VID_SECS,player.getCurrentPosition());
            outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,player.getPlayWhenReady());
        }
        else{
            outState.putLong(Constants.VID_SECS,0);
            outState.putBoolean(Constants.PLAY_WHEN_READY_VAL,true);
        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepArr=savedInstanceState.getParcelableArrayList(Constants.VIDEO_KEY);
        videoPos=savedInstanceState.getInt(Constants.VIDEO_INDEX);
        recObj=savedInstanceState.getParcelable(Constants.STEPS_ACTIVITY_KEY);
        playbackPosition=savedInstanceState.getLong(Constants.VID_SECS);
        playWhenReady=savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY_VAL);
    }

    private void releasePlayer() {
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(findViewById(R.id.recVideoViewLand) !=null){
            if (hasFocus) {
                mPlayerView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }

    }

    public void setFragmentValues(final int value, final ArrayList<Step> step){
        videoPos=value;

        if(!step.get(value).getVideoURL().equals("")){
            FrameLayout frameLayout = findViewById(R.id.video_container);
            frameLayout.setVisibility(View.VISIBLE);
        }
        else{

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        videoFragment = new VideoFragment();
        videoFragment.setAllSteps(step);
        videoFragment.setStepIndex(value);
        fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit();
        tvDesc.setText(step.get(value).getDescription());
        PaginationFragment pageFragment = new PaginationFragment();
        pageFragment.setCurrentPage(value);
        pageFragment.setSteps(step);
        fragmentManager.beginTransaction()
                .replace(R.id.pageinationContainer,pageFragment)
                .commit();


    }



    @Override
    protected void onStart() {
        super.onStart();
        StepsDownloader.downloadRecepies(this, VideoActivity.this, mIdlingResource);
    }



    @Override
    public void onDone() {

    }
}
