package com.sorcererxw.typefaceviews.widgets

import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

class TypefaceSnackbar private constructor(val snackbar: com.google.android.material.snackbar.Snackbar) {

    val duration: Int
        @com.google.android.material.snackbar.Snackbar.Duration
        get() = snackbar.duration

    val view: View
        get() = snackbar.view

    val isShown: Boolean
        get() = snackbar.isShown

    val isShownOrQueued: Boolean
        get() = snackbar.isShownOrQueued

    fun setTextTypeface(typeface: Typeface?): TypefaceSnackbar {
//        val textView = snackbar.view
//                .findViewById(android.support.design.R.id.snackbar_text) as TextView
//        textView.typeface = typeface
        return this
    }

    fun setActionTypeface(typeface: Typeface?): TypefaceSnackbar {
//        val button = snackbar.view
//                .findViewById(android.support.design.R.id.snackbar_action) as Button
//        button.typeface = typeface
        return this
    }

    /** */
    fun setCallback(callback: com.google.android.material.snackbar.Snackbar.Callback): TypefaceSnackbar {
        snackbar.setCallback(callback)
        return this
    }

    fun setAction(@StringRes resId: Int, listener: View.OnClickListener): TypefaceSnackbar {
        snackbar.setAction(resId, listener)
        return this
    }

    fun setAction(text: CharSequence, listener: View.OnClickListener): TypefaceSnackbar {
        snackbar.setAction(text, listener)
        return this
    }

    fun setActionTextColor(colors: ColorStateList): TypefaceSnackbar {
        snackbar.setActionTextColor(colors)
        return this
    }

    fun setActionTextColor(@ColorInt color: Int): TypefaceSnackbar {
        snackbar.setActionTextColor(color)
        return this
    }

    fun setText(message: CharSequence): TypefaceSnackbar {
        snackbar.setText(message)
        return this
    }

    fun setText(@StringRes resId: Int): TypefaceSnackbar {
        snackbar.setText(resId)
        return this
    }

    fun setDuration(@com.google.android.material.snackbar.Snackbar.Duration duration: Int): TypefaceSnackbar {
        snackbar.duration = duration
        return this
    }

    fun show() {
        snackbar.show()
    }

    fun dismiss() {
        snackbar.dismiss()
    }

    companion object {
        fun make(view: View,
                 text: CharSequence,
                 duration: Int): TypefaceSnackbar {
            val snackbar = com.google.android.material.snackbar.Snackbar.make(view, text, duration)
            val typefaceSnackbar = TypefaceSnackbar(snackbar)
            typefaceSnackbar.setTextTypeface(TypefaceViews.regularTypeface)
            typefaceSnackbar.setActionTypeface(TypefaceViews.mediumTypeface)
            return typefaceSnackbar
        }
    }
}
