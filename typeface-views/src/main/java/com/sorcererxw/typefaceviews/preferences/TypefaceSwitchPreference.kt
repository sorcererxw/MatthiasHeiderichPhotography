package com.sorcererxw.typefaceviews.preferences

import android.content.Context
import android.graphics.Typeface
import android.preference.SwitchPreference
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.sorcererxw.typefaceviews.TypefaceViews

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

class TypefaceSwitchPreference : SwitchPreference {

    var view: View? = null
        private set

    private var mTitleTextView: TextView? = null
    private var mSummaryTextView: TextView? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onBindView(view: View) {
        super.onBindView(view)
        this.view = view
        mTitleTextView = this.view!!.findViewById<View>(android.R.id.title) as TextView
        mSummaryTextView = this.view!!.findViewById<View>(android.R.id.summary) as TextView
        mTitleTextView!!.setTextColor(ColorStateLists.TitleColorStateList(context))
        mSummaryTextView!!.setTextColor(ColorStateLists.summaryColorStateList(context))
        if (title != null && title.length > 0) {
            setTitleTypeface(TypefaceViews.regularTypeface)
        }
        if (summary != null && summary.length > 0) {
            setSummaryTypeface(TypefaceViews.regularTypeface)
        }
    }

    fun setTitleTypeface(typeface: Typeface?) {
        if (mTitleTextView == null) {
            return
        }
        mTitleTextView!!.typeface = typeface
    }

    fun setSummaryTypeface(typeface: Typeface?) {
        if (mSummaryTextView == null) {
            return
        }
        mSummaryTextView!!.typeface = typeface
    }
}
