package com.example.skywalker.popularmovies.di.modules;

import com.example.skywalker.popularmovies.di.scope.PerActivity;
import com.example.skywalker.popularmovies.ui.DetailActivity;
import com.example.skywalker.popularmovies.ui.MainActivity;
import com.example.skywalker.popularmovies.ui.ReviewsActivity;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class InjectorModule {
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    @PerActivity
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {DetailActivityModule.class})
    @PerActivity
    abstract DetailActivity bindDetailActivity();

    @ContributesAndroidInjector(modules = {ReviewsActivityModule.class})
    @PerActivity
    abstract ReviewsActivity bindReviewsActivity();
}
