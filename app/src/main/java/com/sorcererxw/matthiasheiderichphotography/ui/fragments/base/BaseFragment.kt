package com.sorcererxw.matthiasheiderichphotography.ui.fragments.base

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/31
 */
abstract class BaseFragment : Fragment(), MHFragment {
    override fun getContext(): Context {
        return super.getContext()!!
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    open fun onThemeChanged() {
        refreshUI()
    }

    override fun refreshUI() {}

    override fun onShow() {
    }

    override fun onToolbarDoubleTap() {
    }
}
