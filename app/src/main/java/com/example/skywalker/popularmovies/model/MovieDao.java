package com.example.skywalker.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.skywalker.popularmovies.model.MovieList;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<MovieList.Movie>> loadMovies();

    @Insert
    Long addToFavorite(MovieList.Movie movie);

    @Delete
    int removeFromFavorite(MovieList.Movie movie);

    @Query("SELECT * FROM favorites WHERE movieId = :movieId")
    LiveData<MovieList.Movie> getMovie(String movieId);
}
