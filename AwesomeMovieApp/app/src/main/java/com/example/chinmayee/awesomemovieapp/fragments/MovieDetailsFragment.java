package com.example.chinmayee.awesomemovieapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chinmayee.awesomemovieapp.R;
import com.example.chinmayee.awesomemovieapp.data.favorites.FavMoviesContract;
import com.example.chinmayee.awesomemovieapp.dto.MovieDetails;
import com.example.chinmayee.awesomemovieapp.util.Constants;
import com.example.chinmayee.awesomemovieapp.util.JSONUtils;
import com.squareup.picasso.Picasso;

import static com.example.chinmayee.awesomemovieapp.util.Constants.OBJ;

/**
 * Created by Chinmayee on 1/8/18.
 */

public class MovieDetailsFragment extends Fragment{
    private static final String TAG="MovieDetailsFragment";
    private ImageView backDrop;
    private TextView title;
    private TextView releaseDate;
    private TextView rating;
    private Button addToFav;
    private TextView desc;
    MovieDetails obj;
    boolean isFav;
    String id;
    Context context;


    public MovieDetailsFragment(){

    }
    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.SAVE_KEY_OBJ_VAL,obj);
        outState.putBoolean("ISFAV",isFav);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movie_details,container,false);
        backDrop=(ImageView)view.findViewById(R.id.backDrop);
        title =(TextView) view.findViewById(R.id.titleMovie);
        releaseDate=(TextView)view.findViewById(R.id.releaseDate);
        rating=(TextView)view.findViewById(R.id.rating);
        addToFav=(Button)view.findViewById(R.id.button);
        desc=(TextView)view.findViewById(R.id.desc);
        MovieDetails obj1=null;

            Bundle bundle= getArguments();
            obj=bundle.getParcelable(OBJ);
            id = obj.getMovieId();
            isFav= JSONUtils.presentInFav(id,getActivity());
            title.setText(obj.getMovieName());
            releaseDate.setText("RELEASE DATE : "+obj.getReleaseDt());
            rating.setText("RATING : "+obj.getRating()+"/10");
            desc.setText(obj.getDesc());
            Picasso.with(getActivity()).load(obj.getBackPath()).resize(700,300).into(backDrop);
            if(isFav)addToFav.setText("REMOVE FROM FAVORITES");
            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence text = "Removed from Favorites";
                    if(isFav){
                        getContext().getContentResolver().delete(FavMoviesContract.MoviesEntry.buildMovieUriWithID(id),null,null);
                        addToFav.setText("ADD TO FAVORITES");
                        isFav=false;
                    }
                    else{
                        getContext().getContentResolver().insert(FavMoviesContract.MoviesEntry.CONTENT_URI,JSONUtils.getContentValuesFromObj(obj));
                        text = "Marked as favorite";
                        isFav=true;
                        addToFav.setText("REMOVE FROM FAVORITES");
                    }

                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });


        return view;
    }








}
