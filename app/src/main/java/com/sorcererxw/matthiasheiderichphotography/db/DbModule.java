package com.sorcererxw.matthiasheiderichphotography.db;


import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

@Module
public class DbModule {
    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper(Application application) {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder().logger(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.v(message);
            }
        }).build();
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase database = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        database.setLoggingEnabled(true);
        return database;
    }

//    @Provides
//    @Singleton
//    DbHelper provideDbHelper(BriteDatabase briteDatabase) {
//        return new DbHelper(briteDatabase);
//    }
}
