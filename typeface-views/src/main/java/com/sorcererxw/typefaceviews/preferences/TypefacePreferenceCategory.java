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

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        if (getTitle() != null && getTitle().length() > 0) {
            setTitleTypeface(TypefaceViews.getMediumTypeface());
        }
        if (getSummary() != null && getSummary().length() > 0) {
            setSummaryTypeface(TypefaceViews.getRegularTypeface());
        }
    }

    public View getView() {
        return mView;
    }

    public void setTitleTypeface(Typeface typeface) {
        if (mView == null) {
            return;
        }
        ((TextView) mView.findViewById(android.R.id.title)).setTypeface(typeface);
    }

    public void setSummaryTypeface(Typeface typeface) {
        if (mView == null) {
            return;
        }
        ((TextView) mView.findViewById(android.R.id.summary)).setTypeface(typeface);
    }
}
