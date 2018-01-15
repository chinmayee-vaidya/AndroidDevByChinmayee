package com.example.chinmayee.awesomemovieapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.chinmayee.awesomemovieapp.MainActivity;
import com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract;
import com.example.chinmayee.awesomemovieapp.dto.MovieDetails;
import com.example.chinmayee.awesomemovieapp.dto.ReviewDetails;
import com.example.chinmayee.awesomemovieapp.dto.TrailerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/6/18.
 */

public class JSONUtils {
    public static final String PATH_IMG="http://image.tmdb.org/t/p/w185/";
    public static final String PATH_IMG2="http://image.tmdb.org/t/p/w780/";

    /**
     *
     * @param id id to be checked
     * @param context associated context
     * @return true if the movie with the corresponding ID is present in favorites
     */
    public static boolean presentInFav(String id, Context context){
        Uri uri = FavMoviesContract.MoviesEntry.buildMovieUriWithID(id);
        Cursor cursor = context.getContentResolver().query(uri,MainActivity.MAIN_MOVIE_PROJECTION,null,null,null);
        if(cursor==null || cursor.getCount()==0)return false;
        else return true;
    }


    /**
     *
     * @param cursor Current cursor
     * @return MovieDetails Object
     */
    public static MovieDetails getMovieDetailsFromCursor(Cursor cursor){
        MovieDetails obj = new MovieDetails();
        obj.setMovieId(cursor.getString(MainActivity.INDEX_MOVIE_ID));
        obj.setMovieName(cursor.getString(MainActivity.INDEX_MOVIE_NAME));
        obj.setPosterPath(cursor.getString(MainActivity.INDEX_POSTER_PATH));
        obj.setBackPath(cursor.getString(MainActivity.INDEX_BACKDROP_PATH));
        obj.setRating(cursor.getString(MainActivity.INDEX_RATING));
        obj.setReleaseDt(cursor.getString(MainActivity.INDEX_RELEASE_DATE));
        obj.setDesc(cursor.getString(MainActivity.INDEX_DESC));
        return obj;
    }

    /**
     *
     * @param resp JSON object values from the URL
     * @return Content Values
     */
    public static void getContentFromJSON(Context context, JSONObject resp, Uri uri){

        try {
            JSONArray movieResults = resp.getJSONArray("results");
            for(int i=0;i<movieResults.length();i++){
                ContentValues retValues = new ContentValues();
                JSONObject obj = (JSONObject) movieResults.get(i);
                String movieId = obj.getString("id");
                String movieName = obj.getString("title");
                String posterPath=PATH_IMG+obj.getString("poster_path").substring(1);
                String backPath = PATH_IMG2+obj.getString("backdrop_path").substring(1);
                String rating = obj.getString("vote_average");
                String releaseDt = obj.getString("release_date");
                String desc = obj.getString("overview");
                retValues.put(FavMoviesContract.MoviesEntry.MOVIE_ID,movieId);
                retValues.put(FavMoviesContract.MoviesEntry.MOVIE_NAME,movieName);
                retValues.put(FavMoviesContract.MoviesEntry.POSTER_PATH,posterPath);
                retValues.put(FavMoviesContract.MoviesEntry.BACKDROP_PATH,backPath);
                retValues.put(FavMoviesContract.MoviesEntry.RATING,rating);
                retValues.put(FavMoviesContract.MoviesEntry.RELEASE_DATE,releaseDt);
                retValues.put(FavMoviesContract.MoviesEntry.DESC,desc);
                context.getContentResolver().insert(uri,retValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param obj Movie Details Object
     * @return ContentValues
     */
    public static ContentValues getContentValuesFromObj(MovieDetails obj) {
        ContentValues retValues = new ContentValues();
        retValues.put(FavMoviesContract.MoviesEntry.MOVIE_ID,obj.getMovieId());
        retValues.put(FavMoviesContract.MoviesEntry.MOVIE_NAME,obj.getMovieName());
        retValues.put(FavMoviesContract.MoviesEntry.POSTER_PATH,obj.getPosterPath());
        retValues.put(FavMoviesContract.MoviesEntry.BACKDROP_PATH,obj.getBackPath());
        retValues.put(FavMoviesContract.MoviesEntry.RATING,obj.getRating());
        retValues.put(FavMoviesContract.MoviesEntry.RELEASE_DATE,obj.getReleaseDt());
        retValues.put(FavMoviesContract.MoviesEntry.DESC,obj.getDesc());
        return retValues;

    }

    /**
     *
     * @param response
     * @return Review Details List from the response
     */

    public static ArrayList<ReviewDetails> getReviewDetails(JSONObject response) {
        ArrayList<ReviewDetails> retReview = new ArrayList<>();
        try {
            JSONArray movieResults = response.getJSONArray("results");
            for(int i=0;i<movieResults.length();i++){
                JSONObject obj = (JSONObject) movieResults.get(i);
                String revId = obj.getString("id");
                String author = obj.getString("author");
                String content = obj.getString("content");
                String url =obj.getString("url");
                ReviewDetails revObj = new ReviewDetails(revId,author,content,url);
                retReview.add(revObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retReview;

    }

    /**
     *
     * @param response
     * @return get trailer list from the response
     */
    public static ArrayList<TrailerDetails> getVideoList(JSONObject response) {
        ArrayList<TrailerDetails> retValues = new ArrayList<>();
        try {
            JSONArray movieResults = response.getJSONArray("results");
            for(int i=0;i<movieResults.length();i++){
                JSONObject obj = (JSONObject) movieResults.get(i);
                String movieName = obj.getString("name");
                String key = obj.getString("key");
                retValues.add(new TrailerDetails(movieName,key));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retValues;
    }
}
