package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.sorcererxw.matthiasheidericphotography.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/16
 */

public class StyleUtil {
    public static int getAccentColorRes(Context context) {
        return getResourceId(R.attr.colorAccent, context);
    }

    public static int getPrimaryTextColorRes(Context context) {
        return getResourceId(R.attr.colorPrimaryText, context);
    }

    public static int getSecondaryTextColor(Context context) {
        return getResourceId(R.attr.colorSecondaryText, context);
    }

    public static int getPrimaryColorRes(Context context) {
        return getResourceId(R.attr.colorPrimary, context);
    }

    public static int getPrimaryDarkColorRes(Context context){
        return getResourceId(R.attr.colorPrimaryDark, context);
    }

    public static int getBackgroundColor(Context context){
        return getResourceId(R.attr.colorBackground, context);
    }



    private static int getResourceId(int resId, Context context) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(resId, typedValue, true);
        return typedValue.resourceId;
    }
}
