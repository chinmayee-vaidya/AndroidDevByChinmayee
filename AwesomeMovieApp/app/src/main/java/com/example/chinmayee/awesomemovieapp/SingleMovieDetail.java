package com.example.chinmayee.awesomemovieapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinmayee.awesomemovieapp.adapters.FragmentSectionsAdapter;
import com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract;
import com.example.chinmayee.awesomemovieapp.dto.MovieDetails;
import com.example.chinmayee.awesomemovieapp.fragments.MovieDetailsFragment;
import com.example.chinmayee.awesomemovieapp.fragments.MovieReviewsFragment;
import com.example.chinmayee.awesomemovieapp.fragments.MovieTrailersFragment;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.example.chinmayee.awesomemovieapp.util.JSONUtils;
import com.squareup.picasso.Picasso;

import static com.example.chinmayee.awesomemovieapp.util.Constants.BACK_IMG_DET;
import static com.example.chinmayee.awesomemovieapp.util.Constants.DESC_DET;
import static com.example.chinmayee.awesomemovieapp.util.Constants.OBJ;
import static com.example.chinmayee.awesomemovieapp.util.Constants.RATING;
import static com.example.chinmayee.awesomemovieapp.util.Constants.RELEASE_DET;
import static com.example.chinmayee.awesomemovieapp.util.Constants.TITLE_DET;

public class SingleMovieDetail extends AppCompatActivity {
    private static final String TAG="MainActivity";
    TextView mTitle;
    ImageView imgView;
    TextView mDesc;
    TextView rate;
    TextView releaseDt;
    Button fvButton;
    MovieDetails obj;
    boolean isFav;
    String mId;
    FragmentSectionsAdapter tabsAdapter;
    MovieDetailsFragment moveDetails;
    private ViewPager mViewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie_detail);
        Log.d(TAG,"On Create fragment....starting");
        mViewpager=(ViewPager)findViewById(R.id.container);
        tabsAdapter=new FragmentSectionsAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity!=null){
            obj =  intentThatStartedThisActivity.getExtras().getParcelable("Movies");
            bundle.putString("id", obj.getMovieId());
            MovieReviewsFragment movRevObj = new MovieReviewsFragment();
            movRevObj.setArguments(bundle);
            movRevObj.setRetainInstance(true);
            MovieTrailersFragment movTrailerObj = new MovieTrailersFragment();
            movTrailerObj.setArguments(bundle);
            movTrailerObj.setRetainInstance(true);

            Bundle detailsBundle = new Bundle();
            detailsBundle.putString(BACK_IMG_DET,obj.getPosterPath());
            detailsBundle.putString(TITLE_DET,obj.getMovieName());
            detailsBundle.putString(RELEASE_DET,obj.getReleaseDt());
            detailsBundle.putString(RATING,obj.getRating());
            detailsBundle.putString(Constants.MOVIE_ID_DET,obj.getMovieId());
            detailsBundle.putString(DESC_DET,obj.getDesc());
            detailsBundle.putParcelable(OBJ,obj);

            moveDetails = new MovieDetailsFragment();
            moveDetails.onAttach(getApplicationContext());
            moveDetails.setArguments(detailsBundle);


            tabsAdapter.addFragment(moveDetails,"DETAILS");
            tabsAdapter.addFragment(movRevObj,"REVIEWS");
            tabsAdapter.addFragment(movTrailerObj,"TRAILERS");
            mViewpager.setAdapter(tabsAdapter);

            TabLayout tabLayout =(TabLayout)findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewpager);
        }
    }






}
