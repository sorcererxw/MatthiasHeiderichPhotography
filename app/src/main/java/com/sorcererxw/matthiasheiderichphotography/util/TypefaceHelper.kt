package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import android.graphics.Typeface

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/24
 */
object TypefaceHelper {
    private var fontDemi: Typeface? = null

    private var fontBook: Typeface? = null

    enum class Type {
        Demi,
        Book
    }

    fun getTypeface(context: Context, type: Type): Typeface? {
        return when (type) {
            TypefaceHelper.Type.Book -> {
                if (fontBook == null) {
                    fontBook = Typeface.createFromAsset(context.assets, "FuturaPTBook.otf")
                }
                fontBook
            }
            TypefaceHelper.Type.Demi -> {
                if (fontDemi == null) {
                    fontDemi = Typeface.createFromAsset(context.assets, "FuturaPTDemi.otf")
                }
                fontDemi
            }
        }
    }
}
