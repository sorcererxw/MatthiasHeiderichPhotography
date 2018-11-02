package com.sorcererxw.typefaceviews.preferences

import android.content.Context
import android.graphics.Typeface
import android.preference.PreferenceCategory
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

class TypefacePreferenceCategory : PreferenceCategory {

    var view: View? = null
        private set

    private var mTitleTextView: TextView? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onBindView(view: View) {
        super.onBindView(view)
        this.view = view
        mTitleTextView = this.view!!.findViewById<View>(android.R.id.title) as TextView
        mTitleTextView!!.setTextColor(ColorStateLists.TitleColorStateList(context))
        if (title != null && title.length > 0) {
            setTitleTypeface(TypefaceViews.mediumTypeface)
        }
    }

    fun setTitleTypeface(typeface: Typeface?) {
        if (mTitleTextView == null) {
            return
        }
        mTitleTextView!!.typeface = typeface
    }
}
