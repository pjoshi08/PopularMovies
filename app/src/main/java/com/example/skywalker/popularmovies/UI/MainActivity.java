package com.example.skywalker.popularmovies.UI;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.example.skywalker.popularmovies.BuildConfig;
import com.example.skywalker.popularmovies.Model.MovieList;
import com.example.skywalker.popularmovies.R;
import com.example.skywalker.popularmovies.AsyncCalls.APIClient;
import com.example.skywalker.popularmovies.AsyncCalls.APIInterface;
import com.example.skywalker.popularmovies.Adapters.ImageAdapter;
import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AAH_FabulousFragment.Callbacks,
        AAH_FabulousFragment.AnimationListener, ImageAdapter.OnItemClickListener {

    // Binding of views
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.lottieAnimationView) LottieAnimationView lottieAnimationView;

    // Binding of resources
    @BindString(R.string.filter_popular) String filter_popular;
    @BindString(R.string.filter_top_rated) String filter_top_rated;
    @BindString(R.string.parcelable_movie_key) String movieDetailsKey;
    @BindString(R.string.img_list_key) String imageListKey;
    @BindString(R.string.applied_filter_key) String appliedFilterKey;
    @BindString(R.string.movie_data_list_key) String movieDataList;

    private ArrayList<MovieList.Movie> results;
    private ArrayList<String> images;
    private APIInterface apiInterface;
    private String applied_filter;
    private ImageAdapter imageAdapter;
    private final Long loadingIndicatorDuration = 2000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        images = new ArrayList<>();
        results = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFabFragment dialogFrag = MyFabFragment.newInstance();
                dialogFrag.setParentFab(fab);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        imageAdapter = new ImageAdapter(this, images, this);
        recyclerView.setAdapter(imageAdapter);

        if(savedInstanceState != null){
            results = savedInstanceState.getParcelableArrayList(movieDataList);
            applied_filter = savedInstanceState.getString(appliedFilterKey);
            images = savedInstanceState.getStringArrayList(imageListKey);
            imageAdapter = new ImageAdapter(this, images, this);
            recyclerView.setAdapter(imageAdapter);
            showLoadingIndicator();
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
        }
    }

    private void getMovies(String sortValue){
        images.clear();
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

                        imageAdapter.notifyDataSetChanged();
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
        Intent movieDetailIntent = new Intent(this, DetailActivity.class);

        MovieList.Movie movie = results.get(clickedItemIndex);
        movieDetailIntent.putExtra(movieDetailsKey, movie);

        startActivity(movieDetailIntent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void showLoadingIndicator() {
        recyclerView.setVisibility(View.INVISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        startLottieAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMovieDetails();
            }
        }, loadingIndicatorDuration);
    }

    private void showMovieDetails() {
        lottieAnimationView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void startLottieAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(loadingIndicatorDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

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
}
