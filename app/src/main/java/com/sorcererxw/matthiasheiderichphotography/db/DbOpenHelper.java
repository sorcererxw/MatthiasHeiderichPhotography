package com.sorcererxw.matthiasheiderichphotography.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, "PROJECT", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
