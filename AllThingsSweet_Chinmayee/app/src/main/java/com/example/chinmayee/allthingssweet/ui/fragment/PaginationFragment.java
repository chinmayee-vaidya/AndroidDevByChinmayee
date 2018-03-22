package com.example.chinmayee.allthingssweet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.Step;
import com.example.chinmayee.allthingssweet.ui.activity.VideoActivity;
import com.example.chinmayee.allthingssweet.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PaginationFragment extends Fragment {

    int currentPage;
    ArrayList<Step> steps;

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }



    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public PaginationFragment() {
        // Required empty public constructor
    }

    @Nullable
    @BindView(R.id.tvIndex)TextView tvIndex;

    @Nullable
    @BindView(R.id.previousButton)TextView prevButton;

    @Nullable
    @BindView(R.id.nextButton)TextView nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagination, container, false);
        ButterKnife.bind(this, view);

        if(savedInstanceState!=null){
            steps=savedInstanceState.getParcelableArrayList(Constants.STEPS_KEY);
            currentPage=savedInstanceState.getInt(Constants.VID_POS);
        }
        tvIndex.setText((currentPage+1)+"/"+steps.size());
        if (currentPage == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        }
        if(currentPage==steps.size()-1){
            nextButton.setVisibility(View.INVISIBLE);
        }
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoActivity)getActivity()).setFragmentValues(currentPage-1, steps);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoActivity)getActivity()).setFragmentValues(currentPage+1, steps);
            }
        });
        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.STEPS_KEY,steps);
        outState.putInt(Constants.VID_POS,currentPage);
        super.onSaveInstanceState(outState);
    }
}
