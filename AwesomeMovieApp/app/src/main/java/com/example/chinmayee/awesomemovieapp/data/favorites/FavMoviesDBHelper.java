package com.example.chinmayee.awesomemovieapp.data.favorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chinmayee on 1/2/18.
 */

public class FavMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 4;

    public FavMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db On create to create new table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavMoviesContract.MoviesEntry.TABLE_NAME + " (" +
                FavMoviesContract.MoviesEntry.MOVIE_ID               + " REAL PRIMARY KEY, " +
                FavMoviesContract.MoviesEntry.MOVIE_NAME + " TEXT NOT NULL, " +
                FavMoviesContract.MoviesEntry.POSTER_PATH + " TEXT NOT NULL, " +
                FavMoviesContract.MoviesEntry.BACKDROP_PATH + " TEXT NOT NULL, " +
                FavMoviesContract.MoviesEntry.RATING + " REAL NOT NULL, " +
                FavMoviesContract.MoviesEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                FavMoviesContract.MoviesEntry.DESC + " TEXT NOT NULL); " ;

        db.execSQL(CREATE_TABLE);
    }


    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     * action items on upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
