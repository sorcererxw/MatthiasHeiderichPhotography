package com.sorcererxw.matthiasheiderichphotography.data.db;

import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

public class Db {

    private BriteDatabase mDatabase;

    public Db(Context context) {
        mDatabase = new SqlBrite.Builder().logger(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        }).build().wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.immediate());
    }

    private Map<String, ProjectDbManager> mProjectsMap = new HashMap<>();

    public ProjectDbManager getProjectDbManager(String project) {
        if (!mProjectsMap.containsKey(project)) {
            mProjectsMap.put(project, new ProjectDbManager(mDatabase, project));
        }
        return mProjectsMap.get(project);
    }

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static String getString(Cursor cursor, String columnName, String fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getString(columnIndex);
        }
        return fallbackValue;
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == 1;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static long getLong(Cursor cursor, String columnName, long fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getLong(columnIndex);
        }
        return fallbackValue;
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static int getInt(Cursor cursor, String columnName, int fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getInt(columnIndex);
        }
        return fallbackValue;
    }

    public static Date getDate(Cursor cursor, String columnName) {
        return new Date(cursor.getLong(cursor.getColumnIndex(columnName)));
    }
}
