package com.sorcererxw.typefaceviews.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.Button

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

class TypefaceButton : Button {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        init()
    }

    fun init() {
        typeface = TypefaceViews.regularTypeface
    }
}
