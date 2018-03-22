package com.example.chinmayee.allthingssweet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.example.chinmayee.allthingssweet.dto.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.ThumbnailUtils.createVideoThumbnail;

/**
 * Created by Chinmayee on 2/25/18.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{
    private final Context mContext;
    List<Step> allSteps;
    RecepieList recObj;
    StepOnClickListener mClickListener;
    int highlightedItem=0;


    public void setHighlightedItem(int highlightedItem) {
        this.highlightedItem = highlightedItem;
    }

    public StepsAdapter(Context mContext, RecepieList recObj, StepOnClickListener mClickListener) {
        this.mContext = mContext;
        this.recObj = recObj;
        this.mClickListener=mClickListener;

    }

    public interface StepOnClickListener {
        void onClick(ArrayList<Step> step, RecepieList obj,int pos);
    }
    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.stepnames_recycler_view,parent,false);
        v.setFocusable(true);
        return new StepsAdapter.StepsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        Step step = allSteps.get(position);
        holder.stepName.setText(step.getShortDescription());
        if(!step.getThumbnailURL().equals("")){
            if(step.getThumbnailURL().endsWith("mp4")){
                Bitmap bMap= ThumbnailUtils.createVideoThumbnail(step.getThumbnailURL(), MediaStore.Video.Thumbnails.MINI_KIND);
                holder.imgView.setImageBitmap(bMap);
            }
            else{
                Picasso.with(mContext).load(step.getThumbnailURL()).resize(300,300).into(holder.imgView);
            }

        }
        if(position==highlightedItem){
            holder.linearLayout.setSelected(true);
        }
        else{
            holder.linearLayout.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        if(allSteps==null)return 0;
        else return allSteps.size();
    }

    public void swapData(List<Step> arr){
        allSteps=arr;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        @BindView(R.id.stepnamecardview)CardView cardView;
        @BindView(R.id.stepnametv) TextView stepName;
        @BindView(R.id.stepimg) ImageView imgView;
        @BindView(R.id.stepsrecviewll)LinearLayout linearLayout;


        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
            itemView.setFocusableInTouchMode(true);
            itemView.setOnClickListener(this);
            cardView.setOnClickListener(this);
            stepName.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(highlightedItem!=pos){
                int prev = highlightedItem;
                highlightedItem=pos;
                notifyItemChanged(prev);
            }
            notifyItemChanged(pos);
            ArrayList<Step> listArr = new ArrayList<>();
            for(Step step : allSteps){
                listArr.add(step);
            }

            mClickListener.onClick(listArr,recObj,pos);
        }
    }
}
