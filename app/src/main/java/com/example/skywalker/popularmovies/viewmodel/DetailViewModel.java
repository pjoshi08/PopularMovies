package com.example.skywalker.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.skywalker.popularmovies.model.MovieDB;
import com.example.skywalker.popularmovies.model.MovieList;

public class DetailViewModel extends ViewModel {

    private LiveData<MovieList.Movie> movie;

    public DetailViewModel(MovieDB db, String movieId) {
        movie = db.movieDao().getMovie(movieId);
    }

    public LiveData<MovieList.Movie> getMovie() {
        return movie;
    }
}
