package com.example.skywalker.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewList {

    @SerializedName("results")
    public ArrayList<Review> results = null;

    public static class Review implements Parcelable{

        @SerializedName("author")
        public String author;

        @SerializedName("content")
        public String reviewContent;

        private Review(Parcel in) {
            author = in.readString();
            reviewContent = in.readString();
        }

        public static final Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(author);
            parcel.writeString(reviewContent);
        }
    }
}
