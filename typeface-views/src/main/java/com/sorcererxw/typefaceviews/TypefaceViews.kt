package com.sorcererxw.typefaceviews

import android.graphics.Typeface

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

object TypefaceViews {

    var mediumTypeface: Typeface? = null
        private set
    var regularTypeface: Typeface? = null
        private set

    enum class TypefaceType {
        MEDIUM,
        REGULAR
    }

    fun install(mediumTypeface: Typeface?,
                regularTypeface: Typeface?) {
        this.mediumTypeface = mediumTypeface
        this.regularTypeface = regularTypeface
    }
}
