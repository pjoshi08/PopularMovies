package com.example.skywalker.popularmovies.UI;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
    @BindView(R.id.movie_detail_parent) ScrollView movieDetailParent;
    @BindView(R.id.iv_poster) ImageView poster;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_vote_avg) TextView voteAvg;
    @BindView(R.id.tv_synopsis) TextView synopsis;
    @BindDrawable(R.drawable.placeholder) Drawable placeholder;
    @BindDrawable(R.drawable.error) Drawable error;

    // Binding of resources
    @BindString(R.string.movie_id) String movieIDKey;
    @BindColor(R.color.colorPrimaryDark) int colorFrom;
    @BindColor(R.color.primary_text) int colorTo;
    @BindString(R.string.vote_avg_text_add) String voteAvgTextAdd;
    @BindString(R.string.parcelable_movie_key) String movieDetailsKey;
    @BindString(R.string.poster_scheme) String scheme;
    @BindString(R.string.poster_authority) String authority;
    @BindString(R.string.append_path_list) String appendEncodedPath;

    private MovieList.Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(movieDetailsKey);
            showMovieDetails();
            return;
        }

        Intent movieDetailIntent = getIntent();
        if(movieDetailIntent.hasExtra(movieDetailsKey))
            movie = movieDetailIntent.getParcelableExtra(movieDetailsKey);

        showMovieDetails();
    }

    /**
     * Method to load movie poster with the help of Picasso
     *
     * @param posterPath used to complete Poster URL for Picasso
     */
    private void loadPoster(String posterPath) {
        Picasso.get()
                .load(buildUri(posterPath))
                .placeholder(placeholder)
                .error(error)
                .into(poster);
    }

    private Uri buildUri(String path){
        return  new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .appendEncodedPath(appendEncodedPath)
                .appendEncodedPath(path)
                .build();
    }

    private void showMovieDetails() {
        Long colorAnimationDuration = 500L;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(colorAnimationDuration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                movieDetailParent.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
        populateUI();
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
