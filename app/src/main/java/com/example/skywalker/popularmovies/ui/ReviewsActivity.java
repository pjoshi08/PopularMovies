package com.example.skywalker.popularmovies.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.skywalker.popularmovies.R;
import com.example.skywalker.popularmovies.adapters.ReviewAdapter;
import com.example.skywalker.popularmovies.model.ReviewList;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ReviewsActivity extends AppCompatActivity {

    // View Binding
    @BindView(R.id.review_list) RecyclerView reviewList;
    @BindView(R.id.no_reviews_tv) TextView tvNoReviews;

    // Resource Binding
    @BindString(R.string.reviews_bundle_key) String reviewsKey;

    // DI
    @Inject ReviewAdapter adapter;
    @Inject LinearLayoutManager layoutManager;

    private ArrayList<ReviewList.Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this);

        reviewList.setLayoutManager(layoutManager);
        reviewList.setAdapter(adapter);

        if(savedInstanceState != null){
            reviews = savedInstanceState.getParcelableArrayList(reviewsKey);

            showReviews();
            return;
        }

        Intent reviewIntent = getIntent();
        if(reviewIntent.hasExtra(reviewsKey))
            reviews = reviewIntent.getParcelableArrayListExtra(reviewsKey);

        showReviews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(reviewsKey, reviews);
    }

    private void showReviews(){
        if(reviews.size() != 0){
            tvNoReviews.setVisibility(View.GONE);
            reviewList.setVisibility(View.VISIBLE);
            adapter.setData(reviews);
        } else {
            tvNoReviews.setVisibility(View.VISIBLE);
            reviewList.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.close_activity)
    public void closeActivity(){
        finish();
    }
}
