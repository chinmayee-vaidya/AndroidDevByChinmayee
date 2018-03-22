package com.example.chinmayee.allthingssweet.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.Ingredient;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chinmayee on 3/11/18.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<RecepieList> recList = intent.getParcelableArrayListExtra(Constants.STEPS_ACTIVITY_KEY);
        return new ListRemoteViewsFactory(this.getApplicationContext(),recList);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        ArrayList<RecepieList> arrList;

        public ListRemoteViewsFactory(Context context, ArrayList<RecepieList>recList){
            this.mContext=context;
            this.arrList=recList;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            arrList=IngredientWidgetService.tempObj;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(arrList==null)return 0;
            else return arrList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RecepieList recObj =arrList.get(position);
            RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
            remoteViews.setTextViewText(R.id.ing_rec_title,recObj.getName());
            List<Ingredient> ingredients=recObj.getIngredients();
            StringBuilder sb = new StringBuilder("");
            for(Ingredient ing: ingredients){
                sb.append("\u25BA");
                sb.append(" ");
                sb.append(ing.getQuantity()+" "+ing.getMeasure()+" "+ing.getIngredient());
                sb.append("\n");
            }
            remoteViews.setTextViewText(R.id.ing_list,sb.toString());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
