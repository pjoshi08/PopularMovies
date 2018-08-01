package com.example.skywalker.popularmovies.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.skywalker.popularmovies.BuildConfig;
import com.example.skywalker.popularmovies.adapters.VideoAdapter;
import com.example.skywalker.popularmovies.asynccalls.APIInterface;
import com.example.skywalker.popularmovies.util.AppExecutors;
import com.example.skywalker.popularmovies.model.MovieDB;
import com.example.skywalker.popularmovies.model.MovieList;
import com.example.skywalker.popularmovies.R;
import com.example.skywalker.popularmovies.model.ReviewList;
import com.example.skywalker.popularmovies.model.VideoList;
import com.example.skywalker.popularmovies.viewmodel.DetailViewModel;
import com.example.skywalker.popularmovies.viewmodel.DetailViewModelFactory;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener{

    // Binding of views
    @BindView(R.id.movie_detail_parent) NestedScrollView movieDetailParent;
    @BindView(R.id.iv_poster) ImageView poster;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_vote_avg) TextView voteAvg;
    @BindView(R.id.tv_synopsis) TextView synopsis;
    @BindView(R.id.video_list) RecyclerView videoList;
    @BindView(R.id.bookmark_icon) CheckBox bookmark;
    @BindView(R.id.no_trailers_tv) TextView tvNoTrailers;

    // Binding of resources
    @BindColor(R.color.colorPrimaryDark) int colorFrom;
    @BindColor(R.color.primary_text) int colorTo;
    @BindString(R.string.vote_avg_text_add) String voteAvgTextAdd;
    @BindString(R.string.parcelable_movie_key) String movieDetailsKey;
    @BindString(R.string.uri_scheme) String scheme;
    @BindString(R.string.poster_authority) String authority;
    @BindString(R.string.append_path_list) String appendEncodedPath;
    @BindString(R.string.app_uri) String appUri;
    @BindString(R.string.web_uri) String webUri;
    @BindString(R.string.youtube_keys) String videoListKey;
    @BindDrawable(R.drawable.placeholder) Drawable placeholder;
    @BindDrawable(R.drawable.error) Drawable error;
    @BindString(R.string.add_to_fav) String addToFav;
    @BindString(R.string.remove_from_fav) String removeFromFav;
    @BindString(R.string.reviews_bundle_key) String reviewsKey;

    // DI
    @Inject Toast toast;
    private MovieList.Movie movie;
    @Inject APIInterface apiInterface;
    @Inject LinearLayoutManager videoLayoutManager;
    @Inject VideoAdapter videoAdapter;
    @Inject ArrayList<String> youtubeKeys;
    @Inject ArrayList<ReviewList.Review> reviews;
    @Inject MovieDB db;
    @Inject Intent reviewsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        videoList.setLayoutManager(videoLayoutManager);
        videoList.setAdapter(videoAdapter);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(movieDetailsKey);
            youtubeKeys = savedInstanceState.getStringArrayList(videoListKey);

            videoAdapter.setKeys(youtubeKeys);
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
                .load(buildPosterUri(posterPath))
                .placeholder(placeholder)
                .error(error)
                .into(poster);
    }

    private Uri buildPosterUri(String path){
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
        colorAnimation.addUpdateListener(animator ->
                movieDetailParent.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();
        populateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(movieDetailsKey, movie);
        outState.putStringArrayList(videoListKey, youtubeKeys);
    }

    private void populateUI(){
        loadPoster(movie.posterPath);
        title.setText(movie.movieTitle);
        showBookmark();
        releaseDate.setText(movie.releaseDate);
        String voteAverage = movie.voteAvg + voteAvgTextAdd;
        voteAvg.setText(voteAverage);
        synopsis.setText(movie.plotSynopsis);
        loadVideos(movie.movieId);
        loadReviews(movie.movieId);
    }

    private void loadVideos(String movieId){
        youtubeKeys.clear();
        videoList.setVisibility(View.VISIBLE);
        tvNoTrailers.setVisibility(View.GONE);

        Call<VideoList> call = apiInterface.getVideos(movieId, BuildConfig.API_KEY);
        call.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                try {
                    VideoList videoList = response.body();
                    if(videoList != null){
                        for(VideoList.Video video : videoList.result){
                            youtubeKeys.add(video.youtubeKey);
                        }

                        videoAdapter.setKeys(youtubeKeys);

                        if(videoList.result.size() == 0) {
                            DetailActivity.this.videoList.setVisibility(View.INVISIBLE);
                            tvNoTrailers.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void loadReviews(String movieId){
        reviews.clear();

        Call<ReviewList> call = apiInterface.getReviews(movieId, BuildConfig.API_KEY);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                ReviewList reviewList = response.body();
                if(reviewList != null){
                    reviews = reviewList.results;
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        String videoId = youtubeKeys.get(clickedItemIndex);
        watchTrailer(videoId);
    }

    private void watchTrailer(String videoId){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, buildVideoUri(appUri, videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, buildVideoUri(webUri, videoId));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    private Uri buildVideoUri(String uri, String videoId){
        return Uri.parse(uri + videoId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bookmark.setOnClickListener(v -> {
            if(!bookmark.isChecked())
                removeFromFavorites();
            else addToFavorites();
        });
    }

    private void addToFavorites(){
        AppExecutors.getInstance().getDiskIO().execute(() -> {
                Long success = db.movieDao().addToFavorite(movie);
                runOnUiThread(() -> {
                    if(success > 0) showToast(addToFav);
                });
        });
    }

    private void removeFromFavorites(){
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            int success = db.movieDao().removeFromFavorite(movie);
            runOnUiThread(() -> {
                if(success > 0) showToast(removeFromFav);
            });
        });
    }

    private void showToast(String msg){
        toast.cancel();

        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showBookmark(){

        DetailViewModelFactory factory = new DetailViewModelFactory(db, movie.movieId);
        DetailViewModel vm = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        vm.getMovie().observe(this, movie -> {
            if(movie != null) bookmark.setChecked(true);
        });
    }

    @OnClick(R.id.btn_see_all)
    public void showReviews(){
        reviewsIntent.putParcelableArrayListExtra(reviewsKey, reviews);
        startActivity(reviewsIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
