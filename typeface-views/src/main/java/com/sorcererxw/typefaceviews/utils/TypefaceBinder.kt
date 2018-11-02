package com.sorcererxw.typefaceviews.utils

import android.graphics.Typeface
import android.widget.TextView

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/24
 */

object TypefaceBinder {

    @JvmOverloads
    fun bindTypeface(textView: TextView, typeface: Typeface? = TypefaceViews.regularTypeface) {
        textView.typeface = typeface
    }

}
