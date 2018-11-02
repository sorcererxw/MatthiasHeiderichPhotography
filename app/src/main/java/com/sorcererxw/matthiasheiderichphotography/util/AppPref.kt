package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
class AppPref private constructor(context: Context) : Pref(context, PREFERENCE_NAME) {
    companion object {
        private var instance: AppPref? = null
        fun getInstance(context: Context): AppPref {
            if (instance == null) {
                instance = AppPref(context.applicationContext)
            }
            return instance!!
        }

        private const val PREFERENCE_NAME = "matthiasheiderich"

        private const val KEY_MUZEI_ROTATE_TIME = "KEY_MUZEI_ROTATE_TIME"
        private const val KEY_MUZEI_ROTATE_CATEGORY = "KEY_MUZEI_ROTATE_CATEGORY"
        private const val KEY_MUZEI_ROTATE_ONLY_WIFI = "KEY_MUZEI_ROTATE_ONLY_WIFI"
        private const val KEY_THEME_NIGHT_MODE = "KEY_THEME_NIGHT_MODE"
        private const val KEY_AUTO_ROTATE_TIME = "KEY_AUTO_ROTATE_TIME"
        private const val KEY_AUTO_ROTATE_ENABLE = "KEY_AUTO_ROTATE_ENABLE"
        private const val KEY_AUTO_ROTATE_WIFI = "KEY_AUTO_ROTATE_WIFI"
    }

    val muzeiRotateTime = getRxSharedPreferences()
            .getLong(KEY_MUZEI_ROTATE_TIME, 1000 * 60 * 60 * 24L)
    val muzeiRotateCategory = getRxSharedPreferences()
            .getString(KEY_MUZEI_ROTATE_CATEGORY, "")
    val muzeiRotateOnlyWifi = getRxSharedPreferences()
            .getBoolean(KEY_MUZEI_ROTATE_ONLY_WIFI, true)
    val themeNightMode = getRxSharedPreferences()
            .getBoolean(KEY_THEME_NIGHT_MODE, false)
    val autoRotateTime = getRxSharedPreferences()
            .getLong(KEY_AUTO_ROTATE_TIME, 1000 * 60 * 60 * 24L)
    val autoRotateEnable = getRxSharedPreferences()
            .getBoolean(KEY_AUTO_ROTATE_ENABLE, false)
    val autoRotateOnlyInWifi = getRxSharedPreferences()
            .getBoolean(KEY_AUTO_ROTATE_WIFI, false)

    fun lastSync(gallery: String) = getRxSharedPreferences()
            .getLong("last_sync_$gallery", 0)
}