package com.sorcererxw.typefaceviews.preferences

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import android.util.TypedValue

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

internal object ColorStateLists {
    fun TitleColorStateList(context: Context): ColorStateList {
        val primaryText = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(android.R.attr.textColorPrimary, primaryText, true)


        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(ContextCompat.getColor(context, primaryText.resourceId),
                        ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, primaryText.resourceId), 64))
        )
    }

    fun summaryColorStateList(context: Context): ColorStateList {
        val primaryText = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(android.R.attr.textColorSecondary, primaryText, true)

        return ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(ContextCompat.getColor(context, primaryText.resourceId),
                        ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, primaryText.resourceId), 64))
        )
    }
}
