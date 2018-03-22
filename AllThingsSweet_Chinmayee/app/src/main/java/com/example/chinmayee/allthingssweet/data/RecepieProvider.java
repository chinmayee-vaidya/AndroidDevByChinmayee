package com.example.chinmayee.allthingssweet.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.chinmayee.allthingssweet.data.RecepieContract.BASE_URI;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.REC_SUFFLIX;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.IS_WID;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.RecepiesColumns.RECEPIE_ID;
import static com.example.chinmayee.allthingssweet.data.RecepieContract.WIDGET_SUFFLIX;
import static com.example.chinmayee.allthingssweet.data.RecepieDBHelper.TABLES.RECEPIE_TB_TITLE;

/**
 * Created by Chinmayee on 3/8/18.
 */

public class RecepieProvider extends ContentProvider {

    public static final int REC= 100;
    public static final int REC_WITH_ID= 101;
    public static final int REC_WIDGET=102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecepieDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BASE_URI,REC_SUFFLIX,REC);
        uriMatcher.addURI(BASE_URI,REC_SUFFLIX+"/#",REC_WITH_ID);
        uriMatcher.addURI(BASE_URI,REC_SUFFLIX+"/"+WIDGET_SUFFLIX,REC_WIDGET);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper=new RecepieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Log.v("TAGG", uri.toString());
        int match = sUriMatcher.match(uri);
        Log.v("MATCH", match+"");
        Cursor retCursor=null;
        switch(match){
            case REC:
                retCursor =  db.query(RECEPIE_TB_TITLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REC_WITH_ID:
                String movie_id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movie_id};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RECEPIE_TB_TITLE,
                        projection,
                        RECEPIE_ID + " = ? ",
                        selectionArguments,
                        null,null,sortOrder);
                break;
            case REC_WIDGET:
                String[] selectionArgumentsWid = new String[]{"YES"};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RECEPIE_TB_TITLE,
                        projection,
                        IS_WID  + " = ? ",
                        selectionArgumentsWid,
                        null,null,sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        Log.v("Cursor", retCursor.getColumnName(4));
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();


        switch (sUriMatcher.match(uri)) {

            case  REC:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {


                        int count = db.update(RECEPIE_TB_TITLE,value,RECEPIE_ID + " = ? ",
                                new String[]{String.valueOf(value.get(RECEPIE_ID))});

                        if(count<=0){
                            value.put(RecepieContract.RecepiesColumns.IS_WID,"NO");
                            long _id = db.insert(RECEPIE_TB_TITLE, null, value);

                            if (_id != -1) {
                                rowsInserted++;
                            }
                        }


                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)){
            case REC:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RECEPIE_TB_TITLE,
                        selection,
                        selectionArgs
                );
                break;
            case REC_WITH_ID:{
                String id = uri.getLastPathSegment();
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(RECEPIE_TB_TITLE,
                        RECEPIE_ID+"=?",new String[]{id});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri+"  "+sUriMatcher.match(uri));
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match){
            case REC_WITH_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};
                rowsUpdated = db.update(RECEPIE_TB_TITLE,
                        values,
                        RECEPIE_ID +"=?",selectionArguments);
                break;
        }
        if ( rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
