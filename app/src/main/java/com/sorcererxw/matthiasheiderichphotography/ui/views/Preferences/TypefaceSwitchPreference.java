package com.sorcererxw.matthiasheiderichphotography.ui.views.Preferences;

import android.content.Context;
import android.preference.SwitchPreference;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class TypefaceSwitchPreference extends SwitchPreference {
    public TypefaceSwitchPreference(Context context, AttributeSet attrs,
                                    int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TypefaceSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefaceSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefaceSwitchPreference(Context context) {
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
