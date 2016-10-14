package com.sorcererxw.matthiasheiderichphotography.ui.views.Preference;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class TypefacePreferenceCategory extends PreferenceCategory {
    public TypefacePreferenceCategory(Context context, AttributeSet attrs,
                                      int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TypefacePreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypefacePreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypefacePreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView title = (TextView) view.findViewById(android.R.id.title);
        title.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Demi));
    }
}
