package com.example.chinmayee.allthingssweet.ui.fragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.Step;
import com.example.chinmayee.allthingssweet.utils.Constants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoFragment extends Fragment{

    @Nullable
    @BindView(R.id.recVideoView)SimpleExoPlayerView  mPlayerView;

    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();
    public ArrayList<Step> allSteps;
    public int stepIndex;


    public void setAllSteps(ArrayList<Step> allSteps) {
        this.allSteps = allSteps;
    }

    public ArrayList<Step> getAllSteps(){
        return this.allSteps;
    }
    public int getStepIndex(){
        return this.stepIndex;
    }


    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public VideoFragment() {
        // Required empty public constructor
    }

    public void initPlayer(){
        Uri uri = Uri.parse(allSteps.get(stepIndex).getVideoURL());
        initializePlayer();
        buildMediaSource(uri);
        preparePlayer();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void buildMediaSource(Uri uri) {
        mediaSource = new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(Constants.VIDEO_CLASS),
                new DefaultExtractorsFactory(), null, null);
    }
    boolean playWhenReady=true;
    int currentWindow = 0;
    long playbackPosition =0;
    MediaSource mediaSource;

    public void setPlayWhenReady(boolean playVal){
        this.playWhenReady=playVal;
    }

    public void setPosition(long position){
        this.playbackPosition=position;
    }

    public long getPlaybackPosition(){
        return player.getCurrentPosition();
    }

    public boolean retuenValuePlayWhenReady(){
        return player.getPlayWhenReady();
    }

    public void preparePlayer(){
        player.prepare(mediaSource, true, false);
    }

    public void initializePlayer(){
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        try {
            if (savedInstanceState != null) {
                allSteps = savedInstanceState.getParcelableArrayList(Constants.STEPS_KEY);
                stepIndex = savedInstanceState.getInt(Constants.VID_POS);
                playbackPosition=savedInstanceState.getLong(Constants.VID_SECS);
            }
            else{
                if (!allSteps.get(stepIndex).getVideoURL().equals("")) {
                    initPlayer();
                } else {
                    mPlayerView.setVisibility(View.INVISIBLE);
                    Guideline guideLine = view.findViewById(R.id.horizontalHalf);
                    guideLine.setVisibility(View.INVISIBLE);
                }
            }

        }catch (Exception e){

        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            allSteps = savedInstanceState.getParcelableArrayList(Constants.STEPS_KEY);
            stepIndex = savedInstanceState.getInt(Constants.VID_POS);
            playbackPosition=savedInstanceState.getLong(Constants.VID_SECS);
        }

    }

    public  void releasePlayer() {
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }
        if(mediaSource!=null){
            mediaSource.releaseSource();
        }

        mPlayerView=null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        //releasePlayer();
    }

    @Override
    public void onResume() {
        if(player!=null)
        player.setPlayWhenReady(playWhenReady);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.STEPS_KEY,allSteps);
        outState.putInt(Constants.VID_POS,stepIndex);

        if(player!=null){
            playWhenReady=player.getPlayWhenReady();
            playbackPosition=player.getCurrentPosition();
            outState.putLong(Constants.VID_SECS,playbackPosition);

        }

        super.onSaveInstanceState(outState);
    }
}
