package com.sorcererxw.matthiasheiderichphotography.muzei

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource
import com.google.android.apps.muzei.api.UserCommand
import com.sorcererxw.matthiasheiderichphotography.room.AppDatabase
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity
import com.sorcererxw.matthiasheiderichphotography.util.AppPref
import com.sorcererxw.matthiasheiderichphotography.util.NetworkUtil

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/12
 */

class MHArtSource : RemoteMuzeiArtSource(SOURCE_NAME) {

    override fun onCreate() {
        super.onCreate()
        setUserCommands(
                UserCommand(USER_COMMAND_NEXT_WALLPAPER, "Next wallpaper"),
                UserCommand(USER_COMMAND_SETTINGS, "Settings"),
                UserCommand(USER_COMMAND_VIEW_IN_APP, "View in application")
        )
    }

    override fun onCustomCommand(id: Int) {
        super.onCustomCommand(id)
        when (id) {
            USER_COMMAND_NEXT_WALLPAPER -> try {
                onTryUpdate(REASON_NEXT_WALLPAPER_BY_USER)
            } catch (e: RemoteMuzeiArtSource.RetryException) {
                e.printStackTrace()
            }
            USER_COMMAND_SETTINGS -> {
                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("fragment_tag", MainActivity.FRAGMENT_TAG_SETTINGS)
                intent.flags = FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
//            USER_COMMAND_VIEW_IN_APP -> {
//                val intent = Intent(this, DetailActivity::class.java)
//                intent.putExtra("link", currentArtwork.token)
//                intent.flags = FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
        }
    }

    override fun onTryUpdate(reason: Int) {
        scheduleUpdate(System.currentTimeMillis() + AppPref.getInstance(this).muzeiRotateTime.get())
        if (AppPref.getInstance(this).muzeiRotateOnlyWifi.get()
                && !NetworkUtil.isWifiConnected(this)
                && reason != REASON_NEXT_WALLPAPER_BY_USER) {
            return
        }

        val category = AppPref.getInstance(this).muzeiRotateCategory.get()
        val photo = if (category.isEmpty()) {
            AppDatabase.getInstance(this).photoDao().random()
        } else {
            AppDatabase.getInstance(this).photoDao().random(category)
        }

        publishArtwork(Artwork.Builder()
                .title(getName(photo.url))
                .token(photo.url)
                .byline("Matthias Heiderich")
                .viewIntent(Intent(Intent.ACTION_VIEW, Uri.parse(photo.url)))
                .imageUri(Uri.parse(photo.url))
                .build())
    }

    companion object {
        private const val SOURCE_NAME = "MatthiasHeiderichArtSource"

        private const val USER_COMMAND_NEXT_WALLPAPER = 0x1
        private const val USER_COMMAND_SETTINGS = 0x2
        private const val USER_COMMAND_VIEW_IN_APP = 0x3
        private const val REASON_NEXT_WALLPAPER_BY_USER = 0x10

        fun getName(uri: String): String {
            val split = uri.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val raw = split[split.size - 1]
            return raw.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        }
    }
}
