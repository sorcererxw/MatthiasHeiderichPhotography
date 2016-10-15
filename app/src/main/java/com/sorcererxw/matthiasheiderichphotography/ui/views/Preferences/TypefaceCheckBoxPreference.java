package com.sorcererxw.matthiasheiderichphotography.ui.views.Preferences;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class TypefaceCheckBoxPreference extends CheckBoxPreference{
    public TypefaceCheckBoxPreference(Context context, AttributeSet attrs,
                                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefaceCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TypefaceCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefaceCheckBoxPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        summaryView.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
    }
}
