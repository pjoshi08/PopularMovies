package com.example.skywalker.popularmovies.asynccalls;

import com.example.skywalker.popularmovies.model.MovieList;
import com.example.skywalker.popularmovies.model.ReviewList;
import com.example.skywalker.popularmovies.model.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    /**
    *  API call to get MovieList based on the sort value passed
    *  @param sortValue to get the movies sorted based on the value passed
    *  @param apiKey API Key required to make the call
    * */
    @GET("movie/{sort_value}")
    Call<MovieList> getMovies(@Path("sort_value") String sortValue, @Query("api_key") String apiKey);

    /**
     * API call to get VideoList based on the movie passed
     * @param movieId: To get the videos of the particular movie
     * @param apiKey API Key required to make the call
     * */
    @GET("movie/{id}/videos")
    Call<VideoList> getVideos(@Path("id") String movieId, @Query("api_key") String apiKey);

    /**
     * API call to get VideoList based on the movie passed
     * @param movieId: To get the videos of the particular movie
     * @param apiKey API Key required to make the call
     * */
    @GET("movie/{id}/reviews")
    Call<ReviewList> getReviews(@Path("id") String movieId, @Query("api_key") String apiKey);
}
