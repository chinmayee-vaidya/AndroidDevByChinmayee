package com.example.chinmayee.allthingssweet.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.chinmayee.allthingssweet.data.RecepieContract;
import com.example.chinmayee.allthingssweet.dto.RecepieList;

import java.util.List;

/**
 * Created by Chinmayee on 3/10/18.
 */

public class JSONUtils {

    public static void insertIntoDB(Context context, List<RecepieList> listRecpies, Uri contentUri) {

        ContentValues[] retData = new ContentValues[listRecpies.size()];
        for(int i=0;i<listRecpies.size();i++){
            ContentValues retValues=new ContentValues();
            RecepieList recObj = listRecpies.get(i);
            retValues.put(RecepieContract.RecepiesColumns.RECEPIE_ID,recObj.getId());
            retValues.put(RecepieContract.RecepiesColumns.RECEPIE_NAME,recObj.getName());
            retValues.put(RecepieContract.RecepiesColumns.REC_SERV_SIZE,recObj.getServings());
            retValues.put(RecepieContract.RecepiesColumns.REC_IMAGE,recObj.getImage());

            retData[i]=retValues;
        }
        int val =context.getContentResolver().bulkInsert(contentUri,retData);
        Log.v("Inserted: ", val+"");
    }

    public static ContentValues getValues(RecepieList recObj, String str) {
        ContentValues retValues = new ContentValues();
        retValues.put(RecepieContract.RecepiesColumns.RECEPIE_ID,recObj.getId());
        retValues.put(RecepieContract.RecepiesColumns.RECEPIE_NAME,recObj.getName());
        retValues.put(RecepieContract.RecepiesColumns.REC_SERV_SIZE,recObj.getServings());
        retValues.put(RecepieContract.RecepiesColumns.REC_IMAGE,recObj.getImage());
        retValues.put(RecepieContract.RecepiesColumns.IS_WID,str);
        return retValues;

    }
}
