package com.example.chinmayee.awesomemovieapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chinmayee.awesomemovieapp.MainActivity;
import com.example.chinmayee.awesomemovieapp.dto.MovieDetails;
import com.example.chinmayee.awesomemovieapp.R;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.example.chinmayee.awesomemovieapp.util.JSONUtils.getMovieDetailsFromCursor;

/**
 * Created by Chinmayee on 10/29/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;



   public interface MovieAdapterOnClickHandler {
       void onClick(MovieDetails movieDet);
   }

    private Cursor mCursor;

    public MovieAdapter(MovieAdapterOnClickHandler mClickHandler, Context context){
        this.mClickHandler = mClickHandler;
        mContext=context;
    }
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_rec_view,parent,false);
        v.setFocusable(true);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        Picasso.with(mContext).load(mCursor.getString(MainActivity.INDEX_POSTER_PATH)).resize(300,300).into(holder.moviePoster);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public ImageView moviePoster;
        private CardView cardView;

        public MovieViewHolder(final View itemView) {
            super(itemView);
            moviePoster=(ImageView) itemView.findViewById(R.id.movie_poster_front_page);
            cardView=(CardView)itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    mCursor.moveToPosition(adapterPosition);
                    MovieDetails movieDet=getMovieDetailsFromCursor(mCursor);
                    mClickHandler.onClick(movieDet);
                }

            });

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            MovieDetails movieDet=getMovieDetailsFromCursor(mCursor);
            mClickHandler.onClick(movieDet);
        }
    }
}
