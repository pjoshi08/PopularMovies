package com.example.skywalker.popularmovies.di.modules;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.example.skywalker.popularmovies.adapters.ReviewAdapter;
import com.example.skywalker.popularmovies.di.qualifiers.ActivityContext;
import com.example.skywalker.popularmovies.ui.ReviewsActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ReviewsActivityModule {

    @Provides
    @ActivityContext
    public Context provideActivityContext(ReviewsActivity activity){
        return activity;
    }

    @Provides
    public ReviewAdapter provideReviewAdapter(@ActivityContext Context context){
        return new ReviewAdapter(context);
    }

    @Provides
    public LinearLayoutManager linearLayoutManager(@ActivityContext Context context){
        return new LinearLayoutManager(context, LinearLayout.VERTICAL, false);
    }
}
