package com.example.chinmayee.allthingssweet.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chinmayee.allthingssweet.R;
import com.example.chinmayee.allthingssweet.dto.RecepieList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chinmayee on 2/24/18.
 */

public class RecepiesAdapter extends RecyclerView.Adapter<RecepiesAdapter.RecepieViewHolder>{
    private final Context mContext;
    List<RecepieList> allRecepies;
    private final RecepieOnClickListener mClickHandler;

    public RecepiesAdapter(Context mContext,RecepieOnClickListener mClickHandler ) {
        this.mContext = mContext;
        this.mClickHandler=mClickHandler;
    }
    public interface RecepieOnClickListener {
        void onClick(RecepieList recepieDet);
    }

    @Override
    public RecepieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recepies_recycler_view,parent,false);
        v.setFocusable(true);
        return new RecepieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecepieViewHolder holder, int position) {
        RecepieList recObj = allRecepies.get(position);
        holder.recName.setText(recObj.getName());
        holder.noOfSteps.setText("Number of Steps: "+recObj.getSteps().size());
        holder.servings.setText("Number of Servings: "+recObj.getServings());
        if(!recObj.getImage().equals("")){
            Picasso.with(mContext).load(recObj.getImage()).resize(300,300).into(holder.circleImageView);
        }
    }

    public void swapAdapter(List<RecepieList> arr){
        allRecepies=arr;
        Log.v("TAG", new String(allRecepies.size()+""));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(allRecepies==null)return 0;
        else return allRecepies.size();
    }

    public class RecepieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        @BindView(R.id.cv)CardView cardView;
        @BindView(R.id.recepie_name) TextView recName;
        @BindView(R.id.steps_total) TextView noOfSteps;
        @BindView(R.id.serving_size) TextView servings;
        @BindView(R.id.item_pic) CircleImageView circleImageView;

        public RecepieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    RecepieList recListCurrent = allRecepies.get(adapterPosition);
                    mClickHandler.onClick(recListCurrent);
                }

            });
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            RecepieList recListCurrent = allRecepies.get(adapterPosition);
            mClickHandler.onClick(recListCurrent);
        }
    }
}
