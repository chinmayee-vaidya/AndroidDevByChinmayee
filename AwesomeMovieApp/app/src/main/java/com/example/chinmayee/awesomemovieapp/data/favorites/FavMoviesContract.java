package com.example.chinmayee.awesomemovieapp.data.favorites;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Chinmayee on 1/2/18.
 */

public class FavMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.chinmayee.awesomemovieapp.data.favorites";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "favorites";

    public static final class MoviesEntry implements BaseColumns{

        //Built Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        //Table name
        public static final String TABLE_NAME = "favorites";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_NAME= "movie_name";
        public static final String POSTER_PATH= "poster_path";
        public static final String BACKDROP_PATH= "back_poster_path";
        public static final String RATING="rating";
        public static final String RELEASE_DATE ="release_date";
        public static final String DESC="description";

        /**
         *
         * @param  id of the movie to be fetched
         * @return single movie details based on the movie ID
         */
        public static Uri buildMovieUriWithID(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

    }

}
