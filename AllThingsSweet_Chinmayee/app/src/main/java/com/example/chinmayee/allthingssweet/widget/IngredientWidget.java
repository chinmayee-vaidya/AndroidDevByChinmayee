package com.example.chinmayee.allthingssweet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.data.RecepieContract;
import com.example.chinmayee.allthingssweet.dto.RecepieAPIInterface;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.ui.activity.StartBakingMain;
import com.example.chinmayee.allthingssweet.ui.activity.StepsActivity;
import com.example.chinmayee.allthingssweet.utils.APIClient;
import com.example.chinmayee.allthingssweet.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.chinmayee.allthingssweet.utils.Constants.TEMP_KEY;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static int tempdata;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<RecepieList> arr) {
        Intent intent = new Intent(context,ListWidgetService.class);
        intent.putExtra(Constants.STEPS_ACTIVITY_KEY,arr);
        intent.putExtra(TEMP_KEY,tempdata++);
        RemoteViews views =new RemoteViews(context.getPackageName(),R.layout.ingredient_widget);
        views.setRemoteAdapter(R.id.list_rec_ing,intent);
        views.setEmptyView(R.id.list_rec_ing,R.id.empty_view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_rec_ing);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        IngredientWidgetService.startActionUpdateWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, IngredientWidgetService.getRecepiesList(context));
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.list_rec_ing);
    }

    public static void updateIngWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,ArrayList<RecepieList>arrList){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, arrList);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.list_rec_ing);
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

