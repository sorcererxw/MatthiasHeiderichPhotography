package com.sorcererxw.typefaceviews.preferences;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class TypefacePreference extends Preference {
    public TypefacePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefacePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefacePreference(Context context) {
        super(context);
    }

    private View mView;

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        if (getTitle() != null && getTitle().length() > 0) {
            setTitleTypeface(TypefaceViews.getRegularTypeface());
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
