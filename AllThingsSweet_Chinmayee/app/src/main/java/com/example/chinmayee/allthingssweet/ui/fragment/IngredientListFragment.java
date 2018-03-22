package com.example.chinmayee.allthingssweet.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.Ingredient;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientListFragment extends Fragment {

    public IngredientListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @BindView(R.id.ingredientsCardView)CardView ingredntsCardView;
    @BindView(R.id.ingListTv)TextView individualIng;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        ButterKnife.bind(this, view);
        RecepieList recepieList = getArguments().getParcelable(Constants.INGREDIENTS_KEY);
        List<Ingredient> ingList = recepieList.getIngredients();
        for(Ingredient ing: ingList){
            individualIng.append("\u25BA");
            individualIng.append(" ");
            individualIng.append(ing.getQuantity()+" "+ing.getMeasure()+" "+ing.getIngredient());
            individualIng.append("\n\n");
        }
        return view;
    }


}
