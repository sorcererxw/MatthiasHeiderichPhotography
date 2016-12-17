package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.widget.Toast;

import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.services.WallpaperChangeService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

public class ScheduleManager {
    private ScheduledExecutorService mScheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private Context mContext;

    public ScheduleManager(Context context) {
        mContext = context;
    }

    public void shutdown() {
        mScheduledExecutorService.shutdown();
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void startAutoChangeWallpaperIfNeeded() {
        if (MHApp.getInstance().getPrefs().getAutoRotateEnable().getValue()) {
            long delay = MHApp.getInstance().getPrefs().getAutoRotateTime().getValue();
            scheduleChangeWallpaperService(delay);
        }
    }

    public void scheduleChangeWallpaperService(long delay) {
        shutdown();
        schedule(mChangeWallpaperRunnable, delay, TimeUnit.MILLISECONDS);
    }

    public void schedule(Runnable runnable, long delay, TimeUnit timeUnit) {
        shutdown();
        mScheduledExecutorService
                .scheduleAtFixedRate(runnable, delay, delay, timeUnit);
    }

    private Runnable mChangeWallpaperRunnable = new Runnable() {
        @Override
        public void run() {
            Timber.d("run");
            WallpaperChangeService.changeWallpaper(mContext);
        }
    };
}
