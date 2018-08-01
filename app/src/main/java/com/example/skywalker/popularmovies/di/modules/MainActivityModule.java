package com.example.skywalker.popularmovies.di.modules;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;

import com.example.skywalker.popularmovies.adapters.ImageAdapter;
import com.example.skywalker.popularmovies.di.qualifiers.ActivityContext;
import com.example.skywalker.popularmovies.di.scope.PerActivity;
import com.example.skywalker.popularmovies.model.MovieList;
import com.example.skywalker.popularmovies.ui.DetailActivity;
import com.example.skywalker.popularmovies.ui.MainActivity;
import com.example.skywalker.popularmovies.viewmodel.MainViewModel;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @Provides
    @ActivityContext
    public Context provideMainActivityContext(MainActivity activity){
        return activity;
    }

    @Provides
    @PerActivity
    public ImageAdapter adapter(@ActivityContext Context context,
                                MainActivity activity){
        return new ImageAdapter(context, activity);
    }

    @Provides
    public GridLayoutManager gridLayoutManager(MainActivity activity){
        return new GridLayoutManager(activity, 2);
    }

    @Provides
    public Intent movieDetailIntent(@ActivityContext Context context){
        return new Intent(context, DetailActivity.class);
    }

    @Provides
    public ArrayList<String> provideArrayListString(){
        return new ArrayList<>();
    }

    @Provides
    public ArrayList<MovieList.Movie> provideArrayListMovie(){
        return new ArrayList<>();
    }

    @Provides
    public MainViewModel provideMainViewModel(MainActivity activity){
        return ViewModelProviders.of(activity).get(MainViewModel.class);
    }
}
