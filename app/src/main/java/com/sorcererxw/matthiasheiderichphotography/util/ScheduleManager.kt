package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import com.sorcererxw.matthiasheiderichphotography.services.WallpaperChangeService
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

class ScheduleManager(private val mContext: Context) {
    private var mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    private val mChangeWallpaperRunnable = Runnable {
        Timber.d("run")
        WallpaperChangeService.changeWallpaper(mContext)
    }

    fun shutdown() {
        mScheduledExecutorService.shutdown()
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    }

    fun startAutoChangeWallpaperIfNeeded() {
        if (AppPref.getInstance(mContext).autoRotateEnable.get()) {
            val delay = AppPref.getInstance(mContext).autoRotateTime.get()
            scheduleChangeWallpaperService(delay)
        }
    }

    fun scheduleChangeWallpaperService(delay: Long) {
        shutdown()
        schedule(mChangeWallpaperRunnable, delay, TimeUnit.MILLISECONDS)
    }

    fun schedule(runnable: Runnable, delay: Long, timeUnit: TimeUnit) {
        shutdown()
        mScheduledExecutorService
                .scheduleAtFixedRate(runnable, delay, delay, timeUnit)
    }
}
