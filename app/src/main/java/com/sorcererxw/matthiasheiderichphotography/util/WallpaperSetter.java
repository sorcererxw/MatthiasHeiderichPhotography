package com.sorcererxw.matthiasheiderichphotography.util;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.app.WallpaperManager.FLAG_LOCK;
import static android.app.WallpaperManager.FLAG_SYSTEM;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/16
 */

public class WallpaperSetter {
    private WallpaperManager mWallpaperManager;

    private Context mContext;

    public WallpaperSetter(Context context) {
        mContext = context;
        mWallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setLockScreenWallpaper(Uri uri) throws IOException {
        setLockScreenWallpaper(uriToBitmap(uri));
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setLockScreenWallpaper(Bitmap bitmap) throws IOException {
        mWallpaperManager.setBitmap(bitmap, null, true, FLAG_LOCK);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setSystemWallpaper(Uri uri) throws IOException {
        setSystemWallpaper(new File(uri.getPath()));
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void setSystemWallpaper(File file) throws IOException {
        mWallpaperManager.setStream(new FileInputStream(file), null, true, FLAG_SYSTEM);
    }

    public void setWallpaperSimple(WindowManager windowManager, Uri uri){
        setWallpaperSimple(windowManager, uriToBitmap(uri));
    }

    public void setWallpaperSimple(WindowManager windowManager, Bitmap wallPaperBitmap) {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        int width = wallPaperBitmap.getWidth();
        width = (width * screenHeight) / wallPaperBitmap.getHeight();
        try {
            mWallpaperManager.setBitmap(
                    Bitmap.createScaledBitmap(wallPaperBitmap, width, screenHeight, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap uriToBitmap(Uri uri) {
        File file = new File(uri.getPath());
        return BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
    }

    private Bitmap cropBitmapFromCenterAndScreenSize(Uri uri) {
        return cropBitmapFromCenterAndScreenSize(uriToBitmap(uri));
    }

    private Bitmap cropBitmapFromCenterAndScreenSize(Bitmap bitmap) {
        float screenWidth, screenHeight;
        float bitmap_width = bitmap.getWidth(), bitmap_height = bitmap
                .getHeight();
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        float bitmap_ratio = bitmap_width / bitmap_height;
        float screen_ratio = screenWidth / screenHeight;
        int bitmapNewWidth, bitmapNewHeight;

        if (screen_ratio > bitmap_ratio) {
            bitmapNewWidth = (int) screenWidth;
            bitmapNewHeight = (int) (bitmapNewWidth / bitmap_ratio);
        } else {
            bitmapNewHeight = (int) screenHeight;
            bitmapNewWidth = (int) (bitmapNewHeight * bitmap_ratio);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapNewWidth,
                bitmapNewHeight, true);

        int bitmapGapX, bitmapGapY;
        bitmapGapX = (int) ((bitmapNewWidth - screenWidth) / 2.0f);
        bitmapGapY = (int) ((bitmapNewHeight - screenHeight) / 2.0f);

        bitmap = Bitmap.createBitmap(bitmap,
                bitmapGapX, bitmapGapY,
                (int) screenWidth, (int) screenHeight);
        return bitmap;
    }

}
