package com.example.chinmayee.allthingssweet.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.adapter.StepsAdapter;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.ui.activity.StepsActivity;
import com.example.chinmayee.allthingssweet.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {

    int pos = 0;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public StepListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.stepName)RecyclerView steps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);
        RecepieList recepieList = getArguments().getParcelable(Constants.STEPS_KEY);
        LinearLayoutManager layoutMngr = new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false);
        StepsAdapter stepAdapter = new StepsAdapter(getContext(),recepieList,(StepsActivity)getActivity());
        stepAdapter.setHighlightedItem(pos);
        steps.setAdapter(stepAdapter);
        steps.setLayoutManager(layoutMngr);
        stepAdapter.swapData(recepieList.getSteps());
        return view;
    }


}
