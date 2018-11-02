package com.sorcererxw.typefaceviews.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

class TypefaceToolbar : Toolbar {

    private var mTitleTypeface = TypefaceViews.mediumTypeface
    private var mSubtitleTypeface = TypefaceViews.regularTypeface

    private var mHasText = true

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context,
                attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        init()
    }

    private fun init() {
        if (mHasText) {
            if (title != null && title.isNotEmpty()) {
                title = title
            }
            if (subtitle != null && subtitle.isNotEmpty()) {
                subtitle = subtitle
            }
        }
    }

    fun setHasText(hasText: Boolean) {
        mHasText = hasText
        if (mHasText) {
            title = title
            subtitle = subtitle
        } else {
            title = ""
            subtitle = ""
        }
    }

    fun setTitleTypeface(type: TypefaceViews.TypefaceType) {
        if (type == TypefaceViews.TypefaceType.MEDIUM) {
            setTitleTypeface(TypefaceViews.mediumTypeface)
        } else if (type == TypefaceViews.TypefaceType.REGULAR) {
            setTitleTypeface(TypefaceViews.regularTypeface)
        }
    }

    fun setTitleTypeface(typeface: Typeface?) {
        mTitleTypeface = typeface
        refreshTitleTypeface()
    }

    fun setSubtitleTypeface(typeface: Typeface) {
        mSubtitleTypeface = typeface
        refreshSubtitleTypeface()
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        refreshTitleTypeface()
    }

    override fun setSubtitle(subtitle: CharSequence) {
        super.setTitle(subtitle)
        refreshSubtitleTypeface()
    }

    private fun refreshTitleTypeface() {
        try {
            val clazz = javaClass.getSuperclass()
            val field = clazz.getDeclaredField("mTitleTextView")
            field.isAccessible = true
            val textView = field.get(this) as TextView
            textView.typeface = mTitleTypeface
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    private fun refreshSubtitleTypeface() {
        try {
            val clazz = javaClass.superclass
            val field = clazz.getDeclaredField("mSubtitleTextView")
            field.isAccessible = true
            val textView = field.get(this) as TextView
            textView.typeface = mSubtitleTypeface
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

}
