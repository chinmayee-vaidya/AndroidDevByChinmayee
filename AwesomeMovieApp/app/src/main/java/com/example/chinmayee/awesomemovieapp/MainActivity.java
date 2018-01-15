package com.example.chinmayee.awesomemovieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chinmayee.awesomemovieapp.adapters.MovieAdapter;
import com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract;
import com.example.chinmayee.awesomemovieapp.data.popular.PopMoviesContract;
import com.example.chinmayee.awesomemovieapp.data.toprated.TopMoviesContract;
import com.example.chinmayee.awesomemovieapp.dto.MovieDetails;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.example.chinmayee.awesomemovieapp.util.JSONUtils;
import com.example.chinmayee.awesomemovieapp.util.NetworkUtils;

import org.json.JSONObject;

import static com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract.MoviesEntry.DESC;
import static com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract.MoviesEntry.MOVIE_ID;
import static com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract.MoviesEntry.MOVIE_NAME;
import static com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract.MoviesEntry.POSTER_PATH;
import static com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract.MoviesEntry.RATING;
import static com.example.chinmayee.awesomemovieapp.data.popular.PopMoviesContract.MoviesEntry.BACKDROP_PATH;
import static com.example.chinmayee.awesomemovieapp.data.popular.PopMoviesContract.MoviesEntry.RELEASE_DATE;


/**
 * Tasks created by Chinmayee Vaidya
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.MovieAdapterOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    RequestQueue requestQueue;
    RecyclerView mrecView;
    MovieAdapter movieAdapter;
    ProgressBar mProgressBar;
    TextView errMsg;
    Parcelable savedState;
    GridLayoutManager layoutManager;
    public static final int MOVIES= 100;
    public static final int MOVIES_WITH_ID= 101;
    public static final String API_KEY="";
    private final String URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    private final String URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    private final String URL_DEFAULT ="http://api.themoviedb.org/3/discover/movie?api_key="+API_KEY;
    private static final String QUERY_KEY="SEARCH_QUERY_KEY";
    private static final int FORECAST_LOADER_ID = 42;
    private static final int FORECAST_LOADER_ID_TOP=24;
    private static final int FORECAST_LOADER_ID_FAV=25;
    private int mPosition = RecyclerView.NO_POSITION;
    private static final String USER_SELECT = "useroption";

    public static final String[] MAIN_MOVIE_PROJECTION = {
        MOVIE_ID,
        MOVIE_NAME,
        POSTER_PATH,
        BACKDROP_PATH,
        RATING,
        RELEASE_DATE,
        DESC,
    };

    public static final int INDEX_MOVIE_ID=0;
    public static final int INDEX_MOVIE_NAME=1;
    public static final int INDEX_POSTER_PATH=2;
    public static final int INDEX_BACKDROP_PATH=3;
    public static final int INDEX_RATING=4;
    public static final int INDEX_RELEASE_DATE=5;
    public static final int INDEX_DESC=6;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(layoutManager!=null){
            outState.putParcelable(Constants.KEY_INSTANCE_STATE_POS,layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            savedState=savedInstanceState.getParcelable(Constants.KEY_INSTANCE_STATE_POS);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mProgressBar=(ProgressBar)findViewById(R.id.pb_loading_indicator);
        mProgressBar.setVisibility(View.VISIBLE);
        mrecView= (RecyclerView) findViewById(R.id.movies);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        layoutManager =
                new GridLayoutManager(this, noOfColumns);
        errMsg=(TextView)findViewById(R.id.tv_error_message_display);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mrecView.setLayoutManager(layoutManager);
        mrecView.setHasFixedSize(true);
        movieAdapter=new MovieAdapter(this,this);
        mrecView.setAdapter(movieAdapter);

        requestQueue = Volley.newRequestQueue(this);




        String url =URL_POPULAR; int loadID =FORECAST_LOADER_ID;

        LoaderManager loaderManager = getSupportLoaderManager();
        if(savedInstanceState!=null){
            savedState=savedInstanceState.getParcelable(Constants.KEY_INSTANCE_STATE_POS);
        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userChoice = sharedPref.getString(Constants.PREF_KEY,Constants.DEF_VALUE);
        if(userChoice==null || userChoice.equals(Constants.POP_VAL)){
            url = URL_POPULAR;
            loadID=FORECAST_LOADER_ID;
        }
        else if(userChoice.equals(Constants.TOP_VAL)){
            url=URL_TOP_RATED;
            loadID=FORECAST_LOADER_ID_TOP;
        }
        else{
            Loader<Cursor> movieSearchLoader = loaderManager.getLoader(FORECAST_LOADER_ID_FAV);
            if(movieSearchLoader==null){
                loaderManager.initLoader(FORECAST_LOADER_ID_FAV, null, this);
            }
            else{
                loaderManager.restartLoader(FORECAST_LOADER_ID_FAV, null, this);
            }

        }
        if(userChoice.equals(Constants.POP_VAL) || userChoice.equals(Constants.TOP_VAL)){

            if(savedState!=null){
                Loader<Cursor> movieSearchLoader = loaderManager.getLoader(loadID);
                if(movieSearchLoader==null){
                    loaderManager.initLoader(loadID, null, this);
                }
                else{
                    loaderManager.restartLoader(loadID, null, this);
                }
            }
            else{
                if(NetworkUtils.isInternetAvailable(this)){
                    JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                            url,null, new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            getApplicationContext().getContentResolver().delete(PopMoviesContract.MoviesEntry.CONTENT_URI,null,null);
                            JSONUtils.getContentFromJSON(getApplicationContext(),response, PopMoviesContract.MoviesEntry.CONTENT_URI);
                        }
                    }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }
                    );

                    requestQueue.add(jsonObjectRequest);
                    Loader<Cursor> movieSearchLoader = loaderManager.getLoader(loadID);
                    if(movieSearchLoader==null){
                        loaderManager.initLoader(loadID, null, this);
                    }
                    else{
                        loaderManager.restartLoader(loadID, null, this);
                    }

                }
                else{
                    showErrorMessage();
                }
            }


        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_select_options, menu);
        return true;
    }


    /**
     * set recycler view true and error msg false
     */
    private void showMovieDataView() {

        errMsg.setVisibility(View.INVISIBLE);
        mrecView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * set recycler view as false and error msg as true
     */
    private void showErrorMessage() {

        mrecView.setVisibility(View.INVISIBLE);
        errMsg.setText(getString(R.string.err_msg));
        errMsg.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        LoaderManager loaderManager = getSupportLoaderManager();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(id==R.id.favorites){
            editor.putString(Constants.PREF_KEY,Constants.FAV_VAL);
            editor.commit();
            Loader<Cursor> movieSearchLoader = loaderManager.getLoader(FORECAST_LOADER_ID_FAV);
            if(movieSearchLoader==null){
                loaderManager.initLoader(FORECAST_LOADER_ID_FAV, null, this);
            }
            else{
                loaderManager.restartLoader(FORECAST_LOADER_ID_FAV, null, this);
            }
        }

        if (id == R.id.popularity) {
            editor.putString(Constants.PREF_KEY,Constants.POP_VAL);
            editor.commit();
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                    URL_POPULAR,null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    getApplicationContext().getContentResolver().delete(PopMoviesContract.MoviesEntry.CONTENT_URI,null,null);
                    JSONUtils.getContentFromJSON(getApplicationContext(),response, PopMoviesContract.MoviesEntry.CONTENT_URI);
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }
            );

            requestQueue.add(jsonObjectRequest);
            Loader<Cursor> movieSearchLoader = loaderManager.getLoader(FORECAST_LOADER_ID);
            if(movieSearchLoader==null){
                loaderManager.initLoader(FORECAST_LOADER_ID, null, this);
            }
            else{
                loaderManager.restartLoader(FORECAST_LOADER_ID, null, this);
            }
            return true;
        }
        else if(id== R.id.top_rated){
            editor.putString(Constants.PREF_KEY,Constants.TOP_VAL);
            editor.commit();
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,
                    URL_TOP_RATED,null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    getApplicationContext().getContentResolver().delete(TopMoviesContract.MoviesEntry.CONTENT_URI,null,null);
                    JSONUtils.getContentFromJSON(getApplicationContext(),response, TopMoviesContract.MoviesEntry.CONTENT_URI);
                    }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }
            );

            requestQueue.add(jsonObjectRequest);
            Loader<Cursor> movieSearchLoader = loaderManager.getLoader(FORECAST_LOADER_ID_TOP);
            if(movieSearchLoader==null){
                loaderManager.initLoader(FORECAST_LOADER_ID_TOP, null, this);
            }
            else{
                loaderManager.restartLoader(FORECAST_LOADER_ID_TOP, null, this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onClick(MovieDetails movieDet) {
        Context context = this;
        Class destinationClass = SingleMovieDetail.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movies",movieDet);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case FORECAST_LOADER_ID:{
                Uri moviesUri = PopMoviesContract.MoviesEntry.CONTENT_URI;
                return new CursorLoader(this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            }
            case FORECAST_LOADER_ID_TOP:{
                {
                    Uri moviesUri = TopMoviesContract.MoviesEntry.CONTENT_URI;
                    return new CursorLoader(this,
                            moviesUri,
                            MAIN_MOVIE_PROJECTION,
                            null,
                            null,
                            null);
                }
            }

            case FORECAST_LOADER_ID_FAV:{
                Uri moviesUri= FavMoviesContract.MoviesEntry.CONTENT_URI;
                return new CursorLoader(this,
                        moviesUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);

            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String userChoice = sharedPref.getString(Constants.PREF_KEY,Constants.DEF_VALUE);

        if(data==null || data.getCount()==0){
            if(userChoice!=null && userChoice.equals(Constants.FAV_VAL)){
                showFavMsg();

            }
            else
                showErrorMessage();
        }
        else{
            showMovieDataView();
            movieAdapter.swapCursor(data);
            if (mPosition == RecyclerView.NO_POSITION)
            if(savedState!=null){
                layoutManager.onRestoreInstanceState(savedState);
            }
            else{
                mrecView.scrollToPosition(0);
            }
        }

    }

    private void showFavMsg() {
        mrecView.setVisibility(View.INVISIBLE);
        errMsg.setText(getString(R.string.fav_err_msg));
        errMsg.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }


}
