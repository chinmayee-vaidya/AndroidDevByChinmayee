package com.example.chinmayee.awesomemovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;


import com.example.chinmayee.awesomemovieapp.R;
import com.example.chinmayee.awesomemovieapp.dto.TrailerDetails;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/7/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    private ArrayList<TrailerDetails> android=new ArrayList<>();
    private Context context;

    public TrailerAdapter(Context context) {
        this.context = context;

    }


    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerDetails movieDet);
    }



    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_rec_view,parent,false);
        return new TrailerViewHolder(v);
    }

    public void setMovieData(ArrayList<TrailerDetails> mDet) {
        android=mDet;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerDetails obj= android.get(position);
        holder.vidName.setText(obj.getMovieName());
        Picasso.with(context).load(Constants.THUMBNAIL_URL+obj.getKey()+Constants.THUMBNAIL_SUFF).resize(300, 300).into(holder.vidImg);

    }

    @Override
    public int getItemCount() {
        if(android==null)return 0;
        else return android.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView vidName;
        ImageView vidImg;
        CardView cardView;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            vidName=(TextView)itemView.findViewById(R.id.trailerName) ;
            vidImg=(ImageView) itemView.findViewById(R.id.trailerThumb) ;
            cardView=(CardView)itemView.findViewById(R.id.card_view_trail);
            itemView.setClickable(true);
            itemView.setFocusableInTouchMode(true);
            itemView.setOnClickListener(this);
            cardView.setOnClickListener(this);
            vidName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String adapterPos = android.get(getAdapterPosition()).getKey();
            String urlPos = Constants.VIDEO_URL+adapterPos;
           context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPos)));
        }
    }
}
