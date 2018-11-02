package com.sorcererxw.matthiasheiderichphotography.ui.others

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/16
 */

class TransparentStatusBarBottomSheetDialog(
        private val mActivity: Activity
) : BottomSheetDialog(mActivity) {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val screenHeight = getScreenHeight(mActivity)
        val statusBarHeight = getStatusBarHeight(context)
        val dialogHeight = screenHeight - statusBarHeight
        if (window != null) {
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    if (dialogHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight)
        }
    }

    private fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}
