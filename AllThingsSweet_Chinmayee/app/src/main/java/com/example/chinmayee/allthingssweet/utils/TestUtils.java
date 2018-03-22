package com.example.chinmayee.allthingssweet.utils;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.Ingredient;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.dto.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chinmayee on 3/16/18.
 */

public class TestUtils {





    public static List<RecepieList> getFakeRecepieData(){


        List<RecepieList> ingList = new ArrayList<>();
        ingList.add(new RecepieList(1,"FAKE_NAME1",getFakeIngData(),getFakeStepData(),8,"FAKE_IMG_1"));
        ingList.add(new RecepieList(2,"FAKE_NAME2",getFakeIngData(),getFakeStepData(),8,"FAKE_IMG_2"));
        ingList.add(new RecepieList(3,"FAKE_NAME3",getFakeIngData(),getFakeStepData(),8,"FAKE_IMG_3"));
        ingList.add(new RecepieList(4,"FAKE_NAME4",getFakeIngData(),getFakeStepData(),8,"FAKE_IMG_4"));
        return ingList;
    }

    public static List<Ingredient> getFakeIngData(){


        List<Ingredient> ingList = new ArrayList<>();
        ingList.add(new Ingredient(3.0,"lb", "FAKE_ING_1"));
        ingList.add(new Ingredient(4.0,"lb", "FAKE_ING_2"));
        ingList.add(new Ingredient(5.0,"lb", "FAKE_ING_3"));
        ingList.add(new Ingredient(6.0,"lb", "FAKE_ING_4"));
        return ingList;


    }

    public static List<Step> getFakeStepData(){

        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step(1,"FAKE_SHORT_DESC1", "FAKE_DESC1","https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4","FAKE_THUMB_URL1"));
        stepList.add(new Step(2,"FAKE_SHORT_DESC2", "FAKE_DESC2","https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4","FAKE_THUMB_URL2"));
        stepList.add(new Step(3,"FAKE_SHORT_DESC3", "FAKE_DESC3","https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4","FAKE_THUMB_URL3"));
        stepList.add(new Step(4,"FAKE_SHORT_DESC4", "FAKE_DESC4","https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4","FAKE_THUMB_URL4"));
        return stepList;
    }
}
