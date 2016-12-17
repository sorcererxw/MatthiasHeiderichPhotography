package com.sorcererxw.typefaceviews.preferences;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

class ColorStateLists {
    static ColorStateList TitleColorStateList(Context context) {
        TypedValue primaryText = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, primaryText, true);


        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled},
                },
                new int[]{
                        ContextCompat.getColor(context, primaryText.resourceId),
                        ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, primaryText.resourceId), 64)
                }
        );
    }

    static ColorStateList summaryColorStateList(Context context) {
        TypedValue primaryText = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.textColorSecondary, primaryText, true);

        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled},
                },
                new int[]{
                        ContextCompat.getColor(context, primaryText.resourceId),
                        ColorUtils.setAlphaComponent(
                                ContextCompat.getColor(context, primaryText.resourceId), 64)
                }
        );
    }
}
