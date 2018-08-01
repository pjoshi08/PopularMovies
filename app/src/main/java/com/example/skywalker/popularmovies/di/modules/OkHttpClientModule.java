package com.example.skywalker.popularmovies.di.modules;

import android.content.Context;

import com.example.skywalker.popularmovies.di.qualifiers.ApplicationContext;
import java.io.File;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class OkHttpClientModule {

    @Provides
    @Singleton
    public File file(@ApplicationContext Context context){
        return new File(context.getCacheDir(), "HttpCache");
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    public Cache cache(File cacheFile){
        return new Cache(cacheFile,
                10 * 1000 * 1000); //10 MB
    }
}
