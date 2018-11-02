package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.util.TypedValue

import com.sorcererxw.matthiasheidericphotography.R

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

private object ThemeHelper {

    @ColorInt
    fun getPrimaryColor(context: Context): Int {
        return ContextCompat.getColor(context, getPrimaryColorRes(context))
    }

    @ColorInt
    fun getPrimaryDarkColor(context: Context): Int {
        return ContextCompat.getColor(context, getPrimaryDarkColorRes(context))
    }

    @ColorInt
    fun getAccentColor(context: Context): Int {
        return ContextCompat.getColor(context, getAccentColorRes(context))
    }

    @ColorInt
    fun getPrimaryTextColor(context: Context): Int {
        return ContextCompat.getColor(context, getPrimaryTextColorRes(context))
    }

    @ColorInt
    fun getSecondaryTextColor(context: Context): Int {
        return ContextCompat.getColor(context, getSecondaryTextColorRes(context))
    }

    @ColorInt
    fun getBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, getBackgroundColorRes(context))
    }

    @ColorInt
    fun getLibCopyrightBackground(context: Context): Int {
        return ContextCompat.getColor(context, getLibCopyrightBackgroundRes(context))
    }

    @ColorInt
    fun getImagePlaceHolderColor(context: Context): Int {
        return ContextCompat.getColor(context, getImagePlaceHolderColorRes(context))
    }

    @ColorInt
    fun getCardColor(context: Context): Int {
        return ContextCompat.getColor(context, getCardColorRes(context))
    }

    fun getPrimaryColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.colorPrimary)
    }

    fun getPrimaryDarkColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.colorPrimaryDark)
    }

    fun getAccentColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.colorAccent)
    }

    fun getPrimaryTextColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.textColorPrimary)
    }

    fun getSecondaryTextColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.textColorSecondary)
    }

    fun getBackgroundColorRes(context: Context): Int {
        return getColorRes(context, android.R.attr.colorBackground)
    }

    fun getLibCopyrightBackgroundRes(context: Context): Int {
        return getColorRes(context, R.attr.colorLibCopyrightBackground)
    }

    fun getImagePlaceHolderColorRes(context: Context): Int {
        return getColorRes(context, R.attr.colorImagePlaceHolder)
    }

    fun getCardColorRes(context: Context): Int {
        return getColorRes(context, R.attr.colorCard)
    }

    private fun getColorRes(context: Context, attrId: Int): Int {
        val theme = context.theme
        val typedValue = TypedValue()
        theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.resourceId
    }
}
