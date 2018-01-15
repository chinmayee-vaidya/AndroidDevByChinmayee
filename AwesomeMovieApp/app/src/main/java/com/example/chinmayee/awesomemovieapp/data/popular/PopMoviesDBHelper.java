package com.example.chinmayee.awesomemovieapp.data.popular;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chinmayee on 1/2/18.
 */

public class PopMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popular.db";
    private static final int DATABASE_VERSION = 4;

    public PopMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db On create to create new table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + PopMoviesContract.MoviesEntry.TABLE_NAME + " (" +
                PopMoviesContract.MoviesEntry.MOVIE_ID               + " REAL PRIMARY KEY, " +
                PopMoviesContract.MoviesEntry.MOVIE_NAME + " TEXT NOT NULL, " +
                PopMoviesContract.MoviesEntry.POSTER_PATH + " TEXT NOT NULL, " +
                PopMoviesContract.MoviesEntry.BACKDROP_PATH + " TEXT NOT NULL, " +
                PopMoviesContract.MoviesEntry.RATING + " REAL NOT NULL, " +
                PopMoviesContract.MoviesEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                PopMoviesContract.MoviesEntry.DESC + " TEXT NOT NULL);" ;
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
        db.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
