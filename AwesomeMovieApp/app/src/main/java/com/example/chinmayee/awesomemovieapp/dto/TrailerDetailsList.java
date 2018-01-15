package com.example.chinmayee.awesomemovieapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/14/18.
 */

public class TrailerDetailsList implements Parcelable {
    ArrayList<TrailerDetails> trailerDetails;

    public TrailerDetailsList(ArrayList<TrailerDetails> trailerDetails) {
        this.trailerDetails = trailerDetails;
    }

    protected TrailerDetailsList(Parcel in) {
        trailerDetails = in.createTypedArrayList(TrailerDetails.CREATOR);
    }

    public static final Creator<TrailerDetailsList> CREATOR = new Creator<TrailerDetailsList>() {
        @Override
        public TrailerDetailsList createFromParcel(Parcel in) {
            return new TrailerDetailsList(in);
        }

        @Override
        public TrailerDetailsList[] newArray(int size) {
            return new TrailerDetailsList[size];
        }
    };

    public ArrayList<TrailerDetails> getTrailerDetails() {
        return trailerDetails;
    }

    public void setTrailerDetails(ArrayList<TrailerDetails> trailerDetails) {
        this.trailerDetails = trailerDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(trailerDetails);
    }
}
