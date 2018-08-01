package com.example.skywalker.popularmovies.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.skywalker.popularmovies.model.MovieDB;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDB db;
    private final String movieId;

    public DetailViewModelFactory(MovieDB db, String movieId){
        this.db = db;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(db, movieId);
    }
}
