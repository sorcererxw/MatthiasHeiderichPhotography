package com.sorcererxw.matthiasheiderichphotography.services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.db.DbHelper;
import com.sorcererxw.matthiasheiderichphotography.db.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.util.NetworkUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.WallpaperSetter;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/14
 */

public class WallpaperChangeService extends IntentService {

    public WallpaperChangeService() {
        super("wallpaper change");
    }

    private WallpaperSetter mWallpaperSetter;

    private static Boolean mRunning = false;

    public static void changeWallpaper(Context context) {
        Timber.d("change wallpaper");
        if (mRunning) {
            Timber.d("running");
            return;
        }
        Intent intent = new Intent(context, WallpaperChangeService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWallpaperSetter = new WallpaperSetter(this);
    }

    private ProjectDBHelper mDBHelper;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (MHApp.getInstance().getPrefs().getAutoRotateOnlyInWifi().getValue()
                && !NetworkUtil.isWifiConnected(this)) {
            return;
        }
        mRunning = true;
        int categoriesIndex = new Random().nextInt(1000);
        final int len = MHApp.PROJECTS_NAME.length;
        categoriesIndex = categoriesIndex % len;
        String projectName = MHApp.PROJECTS_NAME[categoriesIndex];
        getLink(projectName).map(new Func1<List<String>, String>() {
            @Override
            public String call(List<String> list) {
                int photoIndex = new Random().nextInt(1000) % list.size();
                return list.get(photoIndex);
            }
        }).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String uri) {
                try {
                    return Glide.with(WallpaperChangeService.this)
                            .load(uri)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(-1, -1).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).flatMap(new Func1<Bitmap, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Bitmap bitmap) {
                if (bitmap == null) {
                    Toast.makeText(WallpaperChangeService.this,
                            "change wallpaper: failed",
                            Toast.LENGTH_SHORT).show();
                    return Observable.just(false);
                }
                return Observable.just(mWallpaperSetter
                        .setWallpaperSimple((WindowManager) getSystemService(
                                WallpaperChangeService.WINDOW_SERVICE), bitmap));
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mDBHelper.close();
                WallpaperChangeService.this.stopSelf();
                mRunning = false;
            }
        });
    }

    private Observable<List<String>> getLink(String projectName) {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
        mDBHelper = new ProjectDBHelper(this, StringUtil.onlyLetter(projectName));
        MHPreference<Long> lastSync = MHApp.getInstance().getPrefs().getLastSync(projectName, 0L);
        if (System.currentTimeMillis() - lastSync.getValue() < 86400000) {
            List<String> list = mDBHelper.getLinks();
            if (list.size() == 0) {
                lastSync.setValue(0L);
                return getLink(projectName);
            } else {
                return Observable.just(list);
            }
        } else {
            return WebCatcher.catchImageLinks("http://www.matthias-heiderich.de/" + projectName);
        }
    }
}
