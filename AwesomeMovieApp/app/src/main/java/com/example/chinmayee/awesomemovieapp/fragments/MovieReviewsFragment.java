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
import com.example.chinmayee.awesomemovieapp.dto.ReviewDetails;
import com.example.chinmayee.awesomemovieapp.dto.ReviewDetailsList;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.example.chinmayee.awesomemovieapp.util.JSONUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/8/18.
 */

public class MovieReviewsFragment extends Fragment {
    private static final String TAG="MovieReviewsFragment";
    RecyclerView mRecView;
    ReviewAdapter revAdapter;
    RequestQueue requestQueue;
    TextView errMsg;
    ProgressBar mProgressBar;
    LinearLayoutManager layoutManager;
    ArrayList<ReviewDetails>revDet;
    Parcelable recViewPos;
    Parcelable revDetData;

    public MovieReviewsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(layoutManager !=null){
            outState.putParcelable(Constants.KEY_REV_POS,layoutManager.onSaveInstanceState());
        }
        if(revDet!=null){
            outState.putParcelable(Constants.SAVE_REV_DET_KEY,new ReviewDetailsList(revDet));
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_reviews,container,false);
        mRecView=(RecyclerView)view.findViewById(R.id.reviewRecView);
        errMsg=(TextView)view.findViewById(R.id.tv_error_message_display);
        mProgressBar=(ProgressBar)view.findViewById(R.id.pb_loading_indicator) ;
        String id = getArguments().getString("id");
        layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecView.setLayoutManager(layoutManager);
        revAdapter= new ReviewAdapter(getActivity());
        mRecView.setAdapter(revAdapter);
        if(savedInstanceState!=null){
            recViewPos=savedInstanceState.getParcelable(Constants.KEY_REV_POS);
            revDetData=savedInstanceState.getParcelable(Constants.SAVE_REV_DET_KEY);
        }

        if(recViewPos!=null && revDetData!=null){
            showMovieDataView();
            ReviewDetailsList revDetListObj = (ReviewDetailsList)revDetData;
            revDet=revDetListObj.getRevDet();
            revAdapter.setMovieData(revDet);
            layoutManager.onRestoreInstanceState(recViewPos);
        }
        else{
            String revAPIHit="http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+ MainActivity.API_KEY;
            requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                    revAPIHit,null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    revDet = JSONUtils.getReviewDetails(response);
                    if(revDet==null || revDet.size()==0){
                        showErrorMessage();
                    }
                    else{
                        showMovieDataView();
                        revAdapter.setMovieData(revDet);
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
