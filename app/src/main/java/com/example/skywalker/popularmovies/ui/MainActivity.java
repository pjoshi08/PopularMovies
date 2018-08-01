package com.example.skywalker.popularmovies.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.example.skywalker.popularmovies.BuildConfig;
import com.example.skywalker.popularmovies.model.MovieDB;
import com.example.skywalker.popularmovies.model.MovieList;
import com.example.skywalker.popularmovies.R;
import com.example.skywalker.popularmovies.asynccalls.APIInterface;
import com.example.skywalker.popularmovies.adapters.ImageAdapter;
import com.example.skywalker.popularmovies.viewmodel.MainViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AAH_FabulousFragment.Callbacks,
        AAH_FabulousFragment.AnimationListener, ImageAdapter.OnItemClickListener {

    // Binding of views
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.lottieAnimationView) LottieAnimationView lottieAnimationView;
    @BindView(R.id.no_favorites_tv) TextView tvNoFav;

    // Binding of resources
    @BindString(R.string.filter_popular) String filter_popular;
    @BindString(R.string.filter_top_rated) String filter_top_rated;
    @BindString(R.string.filter_favorites) String filter_favorites;
    @BindString(R.string.parcelable_movie_key) String movieDetailsKey;
    @BindString(R.string.img_list_key) String imageListKey;
    @BindString(R.string.applied_filter_key) String appliedFilterKey;
    @BindString(R.string.movie_data_list_key) String movieDataList;

    // DI
    @Inject ArrayList<MovieList.Movie> results;
    @Inject ArrayList<String> images;
    @Inject APIInterface apiInterface;
    private String applied_filter;
    @Inject ImageAdapter imageAdapter;
    private final Long loadingIndicatorDuration = 2000L;
    @Inject GridLayoutManager gridLayoutManager;
    @Inject Intent movieDetailIntent;
    @Inject MovieDB db;
    @Inject MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        fab.setOnClickListener(v -> {
            MyFabFragment dialogFrag = MyFabFragment.newInstance();
            dialogFrag.setParentFab(fab);
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageAdapter);

        if(savedInstanceState != null){
            results = savedInstanceState.getParcelableArrayList(movieDataList);
            applied_filter = savedInstanceState.getString(appliedFilterKey);
            images = savedInstanceState.getStringArrayList(imageListKey);
            imageAdapter.setImages(images);
            showMovieDetails();
            return;
        }

        applied_filter = filter_popular;
        getMovies(filter_popular);
        showLoadingIndicator();
    }

    @Override
    public void onOpenAnimationStart() {}

    @Override
    public void onOpenAnimationEnd() {}

    @Override
    public void onCloseAnimationStart() {}

    @Override
    public void onCloseAnimationEnd() {}

    public String getApplied_filter() {
        return applied_filter;
    }

    @Override
    public void onResult(Object result) {
        applied_filter = result.toString();
        if (result.toString().equalsIgnoreCase(filter_popular)) {
            getMovies(filter_popular);
        } else if(result.toString().equalsIgnoreCase(filter_top_rated)) {
            getMovies(filter_top_rated);
        } else if(result.toString().equalsIgnoreCase(filter_favorites)) {
            getFavorites();
        }
    }

    private void getMovies(String sortValue){
        getMoviesViewHandle();
        Call<MovieList> call = apiInterface.getMovies(sortValue, BuildConfig.API_KEY);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                try {
                    MovieList resource = response.body();
                    if(resource != null) {
                        results = resource.results;
                        for (MovieList.Movie movie : results) {
                            images.add(movie.posterPath);
                        }

                        imageAdapter.setImages(images);
                    }
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onItemClick(int clickedItemIndex) {

        MovieList.Movie movie = results.get(clickedItemIndex);
        movieDetailIntent.putExtra(movieDetailsKey, movie);

        startActivity(movieDetailIntent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void showLoadingIndicator() {
        recyclerView.setVisibility(View.INVISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        startLottieAnimation();
        new Handler().postDelayed(this::showMovieDetails, loadingIndicatorDuration);
    }

    private void showMovieDetails() {
        lottieAnimationView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void startLottieAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(loadingIndicatorDuration);
        animator.addUpdateListener(valueAnimator -> lottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue()));

        if (lottieAnimationView.getProgress() == 0f) {
            animator.start();
        } else {
            lottieAnimationView.setProgress(0f);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(imageListKey, images);
        outState.putString(appliedFilterKey, applied_filter);
        outState.putParcelableArrayList(movieDataList, results);
    }

    private void getFavorites(){

        vm.getMovies().observe(this, movies1 -> {
            if(movies1 != null) {
                if (movies1.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    tvNoFav.setVisibility(View.VISIBLE);
                } else {
                    images.clear();
                    results.clear();

                    for (MovieList.Movie movie : movies1) {
                        images.add(movie.posterPath);
                        results.add(movie);
                    }

                    imageAdapter.setImages(images);
                }
            }
        });
    }

    private void getMoviesViewHandle(){
        images.clear();
        recyclerView.setVisibility(View.VISIBLE);
        tvNoFav.setVisibility(View.GONE);
    }
}
