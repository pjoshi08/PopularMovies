package com.example.skywalker.popularmovies.di.modules;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.skywalker.popularmovies.adapters.VideoAdapter;
import com.example.skywalker.popularmovies.di.qualifiers.ActivityContext;
import com.example.skywalker.popularmovies.model.ReviewList;
import com.example.skywalker.popularmovies.ui.DetailActivity;
import com.example.skywalker.popularmovies.ui.ReviewsActivity;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailActivityModule {

    @Provides
    @ActivityContext
    public Context detailActivityContext(DetailActivity activity){
        return activity;
    }

    @Provides
    public LinearLayoutManager linearLayoutManager(@ActivityContext Context context){
        return new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
    }

    @Provides
    public VideoAdapter videoAdapter(@ActivityContext Context context,
                                     VideoAdapter.OnItemClickListener onItemClickListener){
        return new VideoAdapter(context, onItemClickListener);
    }

    @Provides
    public ArrayList<String> provideArrayList(){
        return new ArrayList<>();
    }

    @Provides
    public VideoAdapter.OnItemClickListener provideClickListener(DetailActivity activity){
        return activity;
    }

    @Provides
    public ArrayList<ReviewList.Review> provideReviewArrayList(){
        return new ArrayList<>();
    }

    @Provides
    public Toast provideToast(@ActivityContext Context context){
        return new Toast(context);
    }

    @Provides
    public Intent startReviewsActivity(@ActivityContext Context context){
        return new Intent(context, ReviewsActivity.class);
    }
}
