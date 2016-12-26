package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.sorcererxw.matthiasheiderichphotography.data.db.Db;
import com.sorcererxw.matthiasheiderichphotography.util.Prefs;
import com.sorcererxw.matthiasheiderichphotography.util.ScheduleManager;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.typefaceviews.TypefaceViews;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class MHApp extends Application {

    private Prefs mPrefs;

    public Prefs getPrefs() {
        return mPrefs;
    }

    private Db mDb;

    public static Db getDb(Context context) {
        return ((MHApp) context.getApplicationContext()).mDb;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        TypefaceViews.install(
                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Demi),
                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book)
        );
        mPrefs = new Prefs(this);
        mDb = new Db(this);

        mScheduleManager = new ScheduleManager(this);
        mScheduleManager.startAutoChangeWallpaperIfNeeded();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private static MHApp mApp;

    public static MHApp getInstance() {
        return mApp;
    }

    private Drawable mTmpDrawable;

    public static void setTmpDrawable(Context context, Drawable drawable) {
        ((MHApp) context.getApplicationContext()).mTmpDrawable = drawable;
    }

    public static Drawable getTmpDrawable(Context context) {
        return ((MHApp) context.getApplicationContext()).mTmpDrawable;
    }

    private ScheduleManager mScheduleManager;

    public ScheduleManager getScheduleManager() {
        return mScheduleManager;
    }
}
