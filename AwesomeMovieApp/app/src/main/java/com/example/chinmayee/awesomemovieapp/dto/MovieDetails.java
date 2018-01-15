package com.example.chinmayee.awesomemovieapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chinmayee on 12/31/17.
 */

public class MovieDetails implements Parcelable {

    String movieId;
    String movieName;
    String posterPath;
    String backPath;
    String rating;
    String releaseDt;
    String desc;

    @Override
    public String toString() {
        return "MovieDetails{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backPath='" + backPath + '\'' +
                ", rating='" + rating + '\'' +
                ", releaseDt='" + releaseDt + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public MovieDetails(){

    }

    public MovieDetails(String movieId, String movieName, String posterPath, String backPath, String rating, String releaseDt, String desc) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterPath = posterPath;
        this.backPath = backPath;
        this.rating = rating;
        this.releaseDt = releaseDt;
        this.desc = desc;
    }


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDt() {
        return releaseDt;
    }

    public void setReleaseDt(String releaseDt) {
        this.releaseDt = releaseDt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    protected MovieDetails(Parcel in) {
        this.movieId=in.readString();
        this.movieName=in.readString();
        this.posterPath = in.readString();
        this.backPath=in.readString();
        this.rating=in.readString();
        this.releaseDt=in.readString();
        this.desc=in.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(movieName);
        dest.writeString(posterPath);
        dest.writeString(backPath);
        dest.writeString(rating);
        dest.writeString(releaseDt);
        dest.writeString(desc);

    }
}

