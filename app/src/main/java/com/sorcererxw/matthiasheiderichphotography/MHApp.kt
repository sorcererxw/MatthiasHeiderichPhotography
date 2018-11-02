package com.sorcererxw.matthiasheiderichphotography

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import com.sorcererxw.matthiasheiderichphotography.util.ScheduleManager
import com.sorcererxw.matthiasheidericphotography.BuildConfig
import timber.log.Timber

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
class MHApp : Application() {

    private var mTmpDrawable: Drawable? = null

    var scheduleManager: ScheduleManager? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

//        TypefaceViews.install(
//                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Demi),
//                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book)
//        )

        scheduleManager = ScheduleManager(this)
        scheduleManager!!.startAutoChangeWallpaperIfNeeded()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        var instance: MHApp? = null
            private set

        fun setTmpDrawable(context: Context, drawable: Drawable) {
            (context.applicationContext as MHApp).mTmpDrawable = drawable
        }

        fun getTmpDrawable(context: Context): Drawable? {
            return (context.applicationContext as MHApp).mTmpDrawable
        }
    }
}
