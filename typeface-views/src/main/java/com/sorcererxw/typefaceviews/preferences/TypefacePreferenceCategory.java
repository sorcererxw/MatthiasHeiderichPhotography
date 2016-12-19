package com.sorcererxw.typefaceviews.preferences;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class TypefacePreferenceCategory extends PreferenceCategory {

    public TypefacePreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefacePreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefacePreferenceCategory(Context context) {
        super(context);
    }

    private View mView;

    private TextView mTitleTextView;

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        mTitleTextView = (TextView) mView.findViewById(android.R.id.title);
        mTitleTextView.setTextColor(ColorStateLists.TitleColorStateList(getContext()));
        if (getTitle() != null && getTitle().length() > 0) {
            setTitleTypeface(TypefaceViews.getMediumTypeface());
        }
    }

    public View getView() {
        return mView;
    }

    public void setTitleTypeface(Typeface typeface) {
        if (mTitleTextView == null) {
            return;
        }
        mTitleTextView.setTypeface(typeface);
    }
}
