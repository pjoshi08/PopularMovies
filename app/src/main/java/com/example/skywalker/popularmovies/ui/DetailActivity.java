package com.example.skywalker.popularmovies.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.skywalker.popularmovies.Model.MovieList;
import com.example.skywalker.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindColor;
import butterknife.BindDrawable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    // Binding of views
    @BindView(R.id.movie_detail_group) ConstraintLayout movieDetailGroup;
    @BindView(R.id.iv_poster) ImageView poster;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_vote_avg) TextView voteAvg;
    @BindView(R.id.tv_synopsis) TextView synopsis;
    @BindDrawable(R.drawable.placeholder) Drawable placeholder;
    @BindDrawable(R.drawable.error) Drawable error;
    @BindView(R.id.lottieAnimationView) LottieAnimationView lottieAnimationView;

    // Binding of resources
    @BindString(R.string.poster_base_url_500w) String posterBaseURL500w;
    @BindString(R.string.movie_id) String movieIDKey;
    @BindColor(R.color.colorPrimaryDark) int colorFrom;
    @BindColor(R.color.primary_text) int colorTo;
    @BindString(R.string.vote_avg_text_add) String voteAvgTextAdd;
    @BindString(R.string.parcelable_movie_key) String movieDetailsKey;

    private final Long loadingIndicatorDuration = 1800L;
    private MovieList.Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(movieDetailsKey);
            showLoadingIndicator();
            return;
        }

        Intent movieDetailIntent = getIntent();
        if(movieDetailIntent.hasExtra(movieDetailsKey))
            movie = movieDetailIntent.getParcelableExtra(movieDetailsKey);

        showLoadingIndicator();
    }

    /**
     * Method to load movie poster with the help of Picasso
     *
     * @param posterPath used to complete Poster URL for Picasso
     */
    private void loadPoster(String posterPath) {
        Picasso.get()
                .load(posterBaseURL500w + posterPath)
                .placeholder(placeholder)
                .error(error)
                .into(poster);
    }

    private void showLoadingIndicator() {
        movieDetailGroup.setVisibility(View.INVISIBLE);
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
        Long colorAnimationDuration = 500L;
        lottieAnimationView.setVisibility(View.INVISIBLE);
        movieDetailGroup.setVisibility(View.VISIBLE);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(colorAnimationDuration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                movieDetailGroup.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
        populateUI();
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
        outState.putParcelable(movieDetailsKey, movie);
    }

    private void populateUI(){
        loadPoster(movie.posterPath);
        title.setText(movie.movieTitle);
        releaseDate.setText(movie.releaseDate);
        String voteAverage = movie.voteAvg + voteAvgTextAdd;
        voteAvg.setText(voteAverage);
        synopsis.setText(movie.plotSynopsis);
    }
}
