package com.example.chinmayee.allthingssweet.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.data.RecepieContract;
import com.example.chinmayee.allthingssweet.dto.RecepieAPIInterface;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.ui.activity.StepsActivity;
import com.example.chinmayee.allthingssweet.utils.APIClient;
import com.example.chinmayee.allthingssweet.utils.JSONUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chinmayee on 3/10/18.
 */

public class IngredientWidgetService extends IntentService {

    public static final String UPDATE_WIDGET="com.example.chinmayee.allthingssweet.acion.UPDATEWIDGET";
    static RecepieAPIInterface apiInterface;
    static ArrayList<RecepieList> tempObj;

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_WIDGET.equals(action)) {
                updatePlantWidget();
            }
        }

    }

    public static void setTemp(ArrayList<RecepieList>obj){
        tempObj=obj;
    }

    public static ArrayList<RecepieList> getRecepiesList(Context context){
        apiInterface = APIClient.getClient().create(RecepieAPIInterface.class);
        Cursor cursor =context.getContentResolver().query(RecepieContract.RecepiesEntry.getWidgetUri(), StepsActivity.REC_COLUMNS,null,null,null);
        Call<List<RecepieList>> call = apiInterface.getAllRecepies();
        final ArrayList<RecepieList> objArr = new ArrayList<>();
        final Set<Integer> vals = new HashSet<>();

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                vals.add(id);

            }while(cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        call.enqueue(new Callback<List<RecepieList>>() {
            @Override
            public void onResponse(Call<List<RecepieList>> call, Response<List<RecepieList>> response) {
                List<RecepieList> listRecpies=response.body();
                for(int i=0;i<listRecpies.size();i++){
                    RecepieList obj = listRecpies.get(i);
                    if(vals.contains(obj.getId())){
                        objArr.add(obj);
                    }

                }
                setTemp(objArr);

            }

            @Override
            public void onFailure(Call<List<RecepieList>> call, Throwable t) {
                throw new Error(t.getMessage());

            }
        });
        return objArr;

    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context,IngredientWidgetService.class);
        intent.setAction(UPDATE_WIDGET);
        context.startService(intent);
    }

    public void updatePlantWidget(){
        apiInterface = APIClient.getClient().create(RecepieAPIInterface.class);
        Cursor cursor =getApplicationContext().getContentResolver().query(RecepieContract.RecepiesEntry.getWidgetUri(), StepsActivity.REC_COLUMNS,null,null,null);
        Call<List<RecepieList>> call = apiInterface.getAllRecepies();
        final ArrayList<RecepieList> objArr = new ArrayList<>();
        final Set<Integer> vals = new HashSet<>();
        final Context context = getApplicationContext();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                vals.add(id);

            }while(cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        call.enqueue(new Callback<List<RecepieList>>() {
            @Override
            public void onResponse(Call<List<RecepieList>> call, Response<List<RecepieList>> response) {
                List<RecepieList> listRecpies=response.body();
                for(int i=0;i<listRecpies.size();i++){
                    RecepieList obj = listRecpies.get(i);
                    if(vals.contains(obj.getId())){
                        objArr.add(obj);
                    }
                }

                setTemp(objArr);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_rec_ing);
                IngredientWidget.updateIngWidget(getApplicationContext(),appWidgetManager,appWidgetIds,objArr);

            }

            @Override
            public void onFailure(Call<List<RecepieList>> call, Throwable t) {
                throw new Error(t.getMessage());

            }
        });


    }
}
