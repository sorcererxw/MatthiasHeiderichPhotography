package com.sorcererxw.typefaceviews.widgets

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

class TypefaceToast(context: Context) : Toast(context) {

    private var mTypeface = TypefaceViews.regularTypeface

    fun setTextTypeface(typeface: Typeface) {
        mTypeface = typeface
    }

    override fun show() {
        val toastLayout = view as LinearLayout
        if (toastLayout != null) {
            val textView = toastLayout.getChildAt(0) as TextView
            textView.typeface = mTypeface
        }
        super.show()
    }
}
