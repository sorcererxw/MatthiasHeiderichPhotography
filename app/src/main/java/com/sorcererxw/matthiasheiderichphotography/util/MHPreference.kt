package com.sorcererxw.matthiasheiderichphotography.util

import android.content.SharedPreferences
import java.util.*

/**
 * Created by Sorcerer on 2016/10/13.
 */

class MHPreference<T>(
        private val mPreferences: SharedPreferences,
        private val mKey: String,
        private val mDefaultValue: T) {

    var value: T
        get() {
            when (mDefaultValue) {
                is String -> return mPreferences.getString(mKey, mDefaultValue as String) as T
                is Int -> return Integer.valueOf(
                        mPreferences.getInt(mKey, mDefaultValue as Int)) as T
                is Float -> return java.lang.Float.valueOf(
                        mPreferences.getFloat(mKey, mDefaultValue as Float)) as T
                is Boolean -> return java.lang.Boolean.valueOf(
                        mPreferences.getBoolean(mKey, mDefaultValue as Boolean)) as T
                is Long -> return java.lang.Long.valueOf(
                        mPreferences.getLong(mKey, mDefaultValue as Long)) as T
                is Date -> {
                    val time = mPreferences.getLong(mKey, (mDefaultValue as Date).time)
                    return Date(time) as T
                }
                else -> throw IllegalArgumentException(
                        "Preference type not implemented $mDefaultValue")
            }
        }
        set(value) {
            val editor = mPreferences.edit()
            when (value) {
                is String -> editor.putString(mKey, value as String)
                is Int -> editor.putInt(mKey, value as Int)
                is Float -> editor.putFloat(mKey, value as Float)
                is Boolean -> editor.putBoolean(mKey, value as Boolean)
                is Long -> editor.putLong(mKey, value as Long)
                is Date -> editor.putLong(mKey, (value as Date).time)
                else -> throw IllegalArgumentException("Preference type not implemented $value")
            }
            editor.apply()
        }

    val isContain: Boolean
        get() = mPreferences.contains(mKey)

    fun remove() {
        mPreferences.edit().remove(mKey).apply()
    }
}
