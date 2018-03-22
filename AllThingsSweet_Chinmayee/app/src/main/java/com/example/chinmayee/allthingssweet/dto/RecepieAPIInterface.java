package com.example.chinmayee.allthingssweet.dto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Chinmayee on 2/24/18.
 */

public interface RecepieAPIInterface {
    @GET("baking.json")
    Call<List<RecepieList>> getAllRecepies();
}
