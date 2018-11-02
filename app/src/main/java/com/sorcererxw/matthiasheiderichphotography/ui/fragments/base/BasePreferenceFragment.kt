package com.sorcererxw.matthiasheiderichphotography.ui.fragments.base

import android.content.Context
import androidx.preference.PreferenceFragmentCompat

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
abstract class BasePreferenceFragment : PreferenceFragmentCompat(), MHFragment {
    override fun getContext(): Context {
        return super.getContext()!!
    }

    override fun refreshUI() {}

    override fun onShow() {}

    override fun onToolbarDoubleTap() {}
}