package com.example.skywalker.popularmovies.AsyncCalls;

import com.example.skywalker.popularmovies.Model.MovieList;

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
}
