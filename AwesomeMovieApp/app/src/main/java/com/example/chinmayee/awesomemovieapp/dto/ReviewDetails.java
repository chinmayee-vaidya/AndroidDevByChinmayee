package com.example.chinmayee.awesomemovieapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chinmayee on 1/7/18.
 */

public class ReviewDetails implements Parcelable {
    String id;
    String author;
    String content;
    String link;

    public ReviewDetails(String id, String author, String content, String link) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.link = link;
    }

    protected ReviewDetails(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        link = in.readString();
    }

    public static final Creator<ReviewDetails> CREATOR = new Creator<ReviewDetails>() {
        @Override
        public ReviewDetails createFromParcel(Parcel in) {
            return new ReviewDetails(in);
        }

        @Override
        public ReviewDetails[] newArray(int size) {
            return new ReviewDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(link);
    }
}
