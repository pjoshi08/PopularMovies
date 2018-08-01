package com.example.skywalker.popularmovies.di.components;

import android.app.Application;

import com.example.skywalker.popularmovies.MyApp;
import com.example.skywalker.popularmovies.di.modules.AppModule;
import com.example.skywalker.popularmovies.di.modules.InjectorModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, InjectorModule.class})
public interface AppComponent extends AndroidInjector<MyApp> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(Application application);
}
