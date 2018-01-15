package com.example.chinmayee.awesomemovieapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Chinmayee on 1/14/18.
 */

public class ReviewDetailsList implements Parcelable {
    ArrayList<ReviewDetails> revDet = new ArrayList<>();

    public ReviewDetailsList(ArrayList<ReviewDetails> revDet) {
        this.revDet = revDet;
    }

    public ArrayList<ReviewDetails> getRevDet() {
        return revDet;
    }

    public void setRevDet(ArrayList<ReviewDetails> revDet) {
        this.revDet = revDet;
    }

    public static Creator<ReviewDetailsList> getCREATOR() {
        return CREATOR;
    }

    protected ReviewDetailsList(Parcel in) {
        revDet = in.createTypedArrayList(ReviewDetails.CREATOR);
    }

    public static final Creator<ReviewDetailsList> CREATOR = new Creator<ReviewDetailsList>() {
        @Override
        public ReviewDetailsList createFromParcel(Parcel in) {
            return new ReviewDetailsList(in);
        }

        @Override
        public ReviewDetailsList[] newArray(int size) {
            return new ReviewDetailsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(revDet);
    }
}
