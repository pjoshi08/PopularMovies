package com.example.skywalker.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.skywalker.popularmovies.model.MovieList;

@Database(entities = MovieList.Movie.class, version = 1, exportSchema = false)
public abstract class MovieDB extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static MovieDB sInstance;

    public static MovieDB getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context,
                        MovieDB.class, MovieDB.DATABASE_NAME)
                        .build();
            }
        }

        return sInstance;
    }

    public abstract MovieDao movieDao();
}
