package com.example.chinmayee.awesomemovieapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chinmayee.awesomemovieapp.MainActivity;
import com.example.chinmayee.awesomemovieapp.R;
import com.example.chinmayee.awesomemovieapp.adapters.ReviewAdapter;
import com.example.chinmayee.awesomemovieapp.adapters.TrailerAdapter;
import com.example.chinmayee.awesomemovieapp.dto.ReviewDetails;
import com.example.chinmayee.awesomemovieapp.dto.TrailerDetails;
import com.example.chinmayee.awesomemovieapp.dto.TrailerDetailsList;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.example.chinmayee.awesomemovieapp.util.JSONUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/8/18.
 */

public class MovieTrailersFragment extends Fragment{
    private static final String TAG="MovieTrailersFragment";
    RecyclerView mRecView;
    TrailerAdapter trailerAdapter;
    RequestQueue requestQueue;
    TextView errMsg;
    LinearLayoutManager layoutManager;
    ProgressBar mProgressBar;
    ArrayList<TrailerDetails> trailerList;
    Parcelable trailerPos;
    Parcelable savedTrailerData;
    public MovieTrailersFragment(){

    }

    /**
     * set recycler view true and error msg false
     */
    private void showMovieDataView() {

        errMsg.setVisibility(View.INVISIBLE);
        mRecView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * set recycler view as false and error msg as true
     */
    private void showErrorMessage() {

        mRecView.setVisibility(View.INVISIBLE);
        errMsg.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(layoutManager!=null){
            outState.putParcelable(Constants.KEY_TRAILER_POS,layoutManager.onSaveInstanceState());
        }
        if(trailerList!=null){
            outState.putParcelable(Constants.SAVE_TRAI_DET_KEY,new TrailerDetailsList(trailerList));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_trailers,container,false);
        mRecView=(RecyclerView)view.findViewById(R.id.videoRecView);
        errMsg=(TextView)view.findViewById(R.id.tv_error_message_display);
        mProgressBar=(ProgressBar)view.findViewById(R.id.pb_loading_indicator) ;
        String id = getArguments().getString("id");
        String trailerAPIHit="http://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+MainActivity.API_KEY;
        layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecView.setLayoutManager(layoutManager);
        trailerAdapter= new TrailerAdapter(getActivity());
        mRecView.setAdapter(trailerAdapter);
        if(savedInstanceState!=null){
            trailerPos=savedInstanceState.getParcelable(Constants.KEY_TRAILER_POS);
            savedTrailerData=savedInstanceState.getParcelable(Constants.SAVE_TRAI_DET_KEY);
        }
        if(trailerPos!=null && savedTrailerData!=null){
            showMovieDataView();
            TrailerDetailsList trailerListObj =(TrailerDetailsList)savedTrailerData;
            trailerList=trailerListObj.getTrailerDetails();
            trailerAdapter.setMovieData(trailerList);
            layoutManager.onRestoreInstanceState(trailerPos);

        }
        else{
            requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                    trailerAPIHit,null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    trailerList = JSONUtils.getVideoList(response);
                    if(trailerList==null || trailerList.size()==0){
                        showErrorMessage();
                    }
                    else{
                        showMovieDataView();
                        trailerAdapter.setMovieData(trailerList);
                    }


                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }
            );

            requestQueue.add(jsonObjectRequest);
        }

        return view;
    }


}
