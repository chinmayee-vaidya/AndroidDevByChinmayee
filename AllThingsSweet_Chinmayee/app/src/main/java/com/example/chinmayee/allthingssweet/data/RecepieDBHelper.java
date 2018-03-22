package com.example.chinmayee.allthingssweet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chinmayee on 3/8/18.
 */

public class RecepieDBHelper extends SQLiteOpenHelper {
    public static final String REC_DATABASE_NAME = "recepies.db";
    public static final String ING_DATABASE_NAME = "ingredients.db";
    public static final String STEP_DATABASE_NAME = "steps.db";

    public static final int DATABASE_VERSION  = 4;
    public RecepieDBHelper(Context context) {
        super(context, REC_DATABASE_NAME, null, DATABASE_VERSION);
    }

    public interface TABLES{
        String RECEPIE_TB_TITLE ="recepies";
        String ING_TB_TITLE ="ingredients";
        String STEP_TB_TITLE ="steps";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLES.RECEPIE_TB_TITLE+" ("+
                RecepieContract.RecepiesColumns.RECEPIE_ID+" INTEGER PRIMARY KEY NOT NULL,"+
                RecepieContract.RecepiesColumns.RECEPIE_NAME+" TEXT NOT NULL,"+
                RecepieContract.RecepiesColumns.REC_SERV_SIZE+" INTEGER NOT NULL,"+
                RecepieContract.RecepiesColumns.REC_IMAGE+" TEXT NOT NULL,"+
                RecepieContract.RecepiesColumns.IS_WID+" TEXT NOT NULL,"+
                " UNIQUE (" + RecepieContract.RecepiesColumns.RECEPIE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+TABLES.ING_TB_TITLE+" ( "+
                RecepieContract.IngredientsColumns.ING_QTY+" REAL NOT NULL,"+
                RecepieContract.IngredientsColumns.ING_MEASURE+" TEXT NOT NULL,"+
                RecepieContract.IngredientsColumns.ING_NAME+" TEXT NOT NULL,"+
                RecepieContract.IngredientsColumns.ING_REC_ID+" INTEGER NOT NULL,"+
                "PRIMARY KEY ("+RecepieContract.IngredientsColumns.ING_REC_ID+","+  RecepieContract.IngredientsColumns.ING_NAME+"))"
        );
        db.execSQL("CREATE TABLE "+TABLES.STEP_TB_TITLE+" ( "+
                RecepieContract.StepColumns.STEP_ID+" INTEGER NOT NULL,"+
                RecepieContract.StepColumns.SHORT_DESC+" INTEGER NOT NULL,"+
                RecepieContract.StepColumns.STEP_DESC+" INTEGER NOT NULL,"+
                RecepieContract.StepColumns.VIDEO_URL+" INTEGER NOT NULL,"+
                RecepieContract.StepColumns.VIDEO_THUM_URL+" INTEGER NOT NULL,"+
                RecepieContract.StepColumns.STEP_REC_ID+" INTEGER NOT NULL,"+
                "PRIMARY KEY ("+RecepieContract.StepColumns.STEP_ID+", "+RecepieContract.StepColumns.STEP_REC_ID+"))"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLES.RECEPIE_TB_TITLE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLES.STEP_TB_TITLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLES.ING_TB_TITLE);
        onCreate(db);
    }
}
