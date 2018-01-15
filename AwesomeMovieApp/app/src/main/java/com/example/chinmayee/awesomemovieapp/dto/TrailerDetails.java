package com.example.chinmayee.awesomemovieapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chinmayee on 1/7/18.
 */

public class TrailerDetails implements Parcelable {
    public String movieName;
    public String key;

    public TrailerDetails(String movieName, String key) {
        this.movieName = movieName;
        this.key = key;
    }

    protected TrailerDetails(Parcel in) {
        movieName = in.readString();
        key = in.readString();
    }

    public static final Creator<TrailerDetails> CREATOR = new Creator<TrailerDetails>() {
        @Override
        public TrailerDetails createFromParcel(Parcel in) {
            return new TrailerDetails(in);
        }

        @Override
        public TrailerDetails[] newArray(int size) {
            return new TrailerDetails[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(key);
    }
}
