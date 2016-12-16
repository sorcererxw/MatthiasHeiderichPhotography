package com.sorcererxw.matthiasheiderichphotography.services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.util.WallpaperSetter;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mWallpaperSetter = new WallpaperSetter(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = "http://www.matthias-heiderich.de/";
        int categoriesIndex = new Random().nextInt(1000);
        final int len = MHApp.PROJECTS_NAME.length;
        categoriesIndex = categoriesIndex % len;
        url += MHApp.PROJECTS_NAME[categoriesIndex];
        WebCatcher.catchImageLinks(url)
                .map(new Func1<List<String>, String>() {
                    @Override
                    public String call(List<String> list) {
                        Timber.d("catched list");

                        int photoIndex = new Random().nextInt(1000) % list.size();
                        return list.get(photoIndex);
                    }
                })
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String uri) {
                        Timber.d("catched uri");

                        try {
                            return Glide.with(WallpaperChangeService.this)
                                    .load(uri).asBitmap().into(-1, -1).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        if (bitmap == null) {
                            Toast.makeText(WallpaperChangeService.this,
                                    "change wallpaper: failed",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mWallpaperSetter.setWallpaperSimple((WindowManager) getSystemService(
                                WallpaperChangeService.WINDOW_SERVICE), bitmap);
                    }
                });
    }

}
