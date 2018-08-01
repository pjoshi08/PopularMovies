package com.example.skywalker.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.skywalker.popularmovies.model.MovieDB;
import com.example.skywalker.popularmovies.model.MovieList;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieList.Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        MovieDB db = MovieDB.getsInstance(this.getApplication());
        movies = db.movieDao().loadMovies();
    }

    public LiveData<List<MovieList.Movie>> getMovies() {
        return movies;
    }
}
