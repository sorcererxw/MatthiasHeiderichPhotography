package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
abstract class Pref(
        context: Context,
        preferenceName: String
) {
    private val preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    private var rxSharedPreferences = RxSharedPreferences.create(preferences)

    protected fun getPreferences() = preferences
    protected fun getRxSharedPreferences(): RxSharedPreferences = rxSharedPreferences
}