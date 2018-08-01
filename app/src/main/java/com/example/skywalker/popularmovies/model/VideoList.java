package com.example.skywalker.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoList {

    @SerializedName("results")
    public ArrayList<Video> result;

    public class Video {

        @SerializedName("key")
        public String youtubeKey;
    }
}
