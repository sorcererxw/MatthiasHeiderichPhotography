package com.sorcererxw.matthiasheiderichphotography.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sorcererxw.matthiasheiderichphotography.config.GlideApp
import com.sorcererxw.matthiasheiderichphotography.room.AppDatabase
import com.sorcererxw.matthiasheiderichphotography.util.AppPref
import com.sorcererxw.matthiasheiderichphotography.util.NetworkUtil
import com.sorcererxw.matthiasheiderichphotography.util.WallpaperSetter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/14
 */

class WallpaperChangeService : IntentService("wallpaper change") {

    private lateinit var mWallpaperSetter: WallpaperSetter

    override fun onCreate() {
        super.onCreate()
        mWallpaperSetter = WallpaperSetter(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        if (AppPref.getInstance(applicationContext).autoRotateOnlyInWifi.get()
                && !NetworkUtil.isWifiConnected(this)) {
            return
        }
        mRunning = true
        io.reactivex.Observable.just(
                AppDatabase.getInstance(applicationContext).photoDao().random().url)
                .map { uri ->
                    GlideApp.with(this@WallpaperChangeService)
                            .asBitmap()
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .submit(-1, -1)
                            .get()
                }
                .flatMap { bitmap ->
                    io.reactivex.Observable.just(
                            mWallpaperSetter.setWallpaperSimple(
                                    getSystemService(WINDOW_SERVICE) as WindowManager, bitmap))
                }
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Boolean) {
                        stopSelf()
                        mRunning = false
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }

                })
    }

    companion object {

        private var mRunning: Boolean? = false

        fun changeWallpaper(context: Context) {
            Timber.d("change wallpaper")
            if (mRunning!!) {
                Timber.d("running")
                return
            }
            val intent = Intent(context, WallpaperChangeService::class.java)
            context.startService(intent)
        }
    }
}
