package com.sorcererxw.typefaceviews.preferences;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class TypefaceSwitchPreference extends SwitchPreference {

    public TypefaceSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefaceSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefaceSwitchPreference(Context context) {
        super(context);
    }

    private View mView;

    private TextView mTitleTextView;
    private TextView mSummaryTextView;

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        mTitleTextView = (TextView) mView.findViewById(android.R.id.title);
        mSummaryTextView = (TextView) mView.findViewById(android.R.id.summary);
        mTitleTextView.setTextColor(ColorStateLists.TitleColorStateList(getContext()));
        mSummaryTextView.setTextColor(ColorStateLists.summaryColorStateList(getContext()));
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
        if (mTitleTextView == null) {
            return;
        }
        mTitleTextView.setTypeface(typeface);
    }

    public void setSummaryTypeface(Typeface typeface) {
        if (mSummaryTextView == null) {
            return;
        }
        mSummaryTextView.setTypeface(typeface);
    }
}
