package com.example.chinmayee.awesomemovieapp.data.toprated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Chinmayee on 1/2/18.
 */

public class TopMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 4;

    public TopMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db On create to create new table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + TopMoviesContract.MoviesEntry.TABLE_NAME + " (" +
                TopMoviesContract.MoviesEntry.MOVIE_ID               + " REAL PRIMARY KEY, " +
                TopMoviesContract.MoviesEntry.MOVIE_NAME + " TEXT NOT NULL, " +
                TopMoviesContract.MoviesEntry.POSTER_PATH + " TEXT NOT NULL, " +
                TopMoviesContract.MoviesEntry.BACKDROP_PATH + " TEXT NOT NULL, " +
                TopMoviesContract.MoviesEntry.RATING + " REAL NOT NULL, " +
                TopMoviesContract.MoviesEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                TopMoviesContract.MoviesEntry.DESC + " TEXT NOT NULL);" ;

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
        db.execSQL("DROP TABLE IF EXISTS " + TopMoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
