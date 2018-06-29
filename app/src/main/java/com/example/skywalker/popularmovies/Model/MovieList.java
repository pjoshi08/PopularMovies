package com.example.skywalker.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieList {

    @SerializedName("results")
    public ArrayList<Movie> results = null;

    public static class Movie implements Parcelable {

        @SerializedName("title")
        public String movieTitle;

        @SerializedName("release_date")
        public String releaseDate;

        @SerializedName("poster_path")
        public String posterPath;

        @SerializedName("vote_average")
        public String voteAvg;

        @SerializedName("overview")
        public String plotSynopsis;

        private Movie(Parcel in) {
            movieTitle = in.readString();
            releaseDate = in.readString();
            posterPath = in.readString();
            voteAvg = in.readString();
            plotSynopsis = in.readString();
        }

        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(movieTitle);
            dest.writeString(releaseDate);
            dest.writeString(posterPath);
            dest.writeString(voteAvg);
            dest.writeString(plotSynopsis);
        }
    }
}
