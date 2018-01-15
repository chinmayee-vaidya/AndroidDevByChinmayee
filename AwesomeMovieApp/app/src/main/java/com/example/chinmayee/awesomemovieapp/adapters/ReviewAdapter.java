package com.example.chinmayee.awesomemovieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chinmayee.awesomemovieapp.R;
import com.example.chinmayee.awesomemovieapp.dto.ReviewDetails;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/7/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<ReviewDetails> android=new ArrayList<>();
    private Context context;


    public ReviewAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_rec_view,parent,false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewDetails revDetObj = android.get(position);
        holder.authorView.setText(revDetObj.getAuthor());
        holder.desc.setText(revDetObj.getContent());
        holder.linkView.setText(revDetObj.getLink());
        Linkify.addLinks(holder.linkView, Linkify.WEB_URLS);
    }

    @Override
    public int getItemCount() {
        if(android==null)return 0;
        else return android.size();
    }

    public void setMovieData(ArrayList<ReviewDetails> mDet) {
        android=mDet;
        notifyDataSetChanged();
    }

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewDetails movieDet);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView authorView;
        TextView desc;
        TextView linkView;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorView=(TextView)itemView.findViewById(R.id.authorName);
            desc=(TextView)itemView.findViewById(R.id.descripRev);
            linkView=(TextView)itemView.findViewById(R.id.linkRev);
            itemView.setClickable(true);
            itemView.setFocusableInTouchMode(true);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
