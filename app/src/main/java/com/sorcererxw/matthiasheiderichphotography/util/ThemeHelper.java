package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.sorcererxw.matthiasheidericphotography.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

public class ThemeHelper {

    @ColorInt
    public static int getPrimaryColor(Context context) {
        return ContextCompat.getColor(context, getPrimaryColorRes(context));
    }

    @ColorInt
    public static int getPrimaryDarkColor(Context context) {
        return ContextCompat.getColor(context, getPrimaryDarkColorRes(context));
    }

    @ColorInt
    public static int getAccentColor(Context context) {
        return ContextCompat.getColor(context, getAccentColorRes(context));
    }

    @ColorInt
    public static int getPrimaryTextColor(Context context) {
        return ContextCompat.getColor(context, getPrimaryTextColorRes(context));
    }

    @ColorInt
    public static int getSecondaryTextColor(Context context) {
        return ContextCompat.getColor(context, getSecondaryTextColorRes(context));
    }

    @ColorInt
    public static int getBackgroundColor(Context context) {
        return ContextCompat.getColor(context, getBackgroundColorRes(context));
    }

    @ColorInt
    public static int getLibCopyrightBackground(Context context) {
        return ContextCompat.getColor(context, getLibCopyrightBackgroundRes(context));
    }

    @ColorInt
    public static int getImagePlaceHolderColor(Context context) {
        return ContextCompat.getColor(context, getImagePlaceHolderColorRes(context));
    }

    @ColorInt
    public static int getCardColor(Context context) {
        return ContextCompat.getColor(context, getCardColorRes(context));
    }

    public static int getPrimaryColorRes(Context context) {
        return getColorRes(context, android.R.attr.colorPrimary);
    }

    public static int getPrimaryDarkColorRes(Context context) {
        return getColorRes(context, android.R.attr.colorPrimaryDark);
    }

    public static int getAccentColorRes(Context context) {
        return getColorRes(context, android.R.attr.colorAccent);
    }

    public static int getPrimaryTextColorRes(Context context) {
        return getColorRes(context, android.R.attr.textColorPrimary);
    }

    public static int getSecondaryTextColorRes(Context context) {
        return getColorRes(context, android.R.attr.textColorSecondary);
    }

    public static int getBackgroundColorRes(Context context) {
        return getColorRes(context, android.R.attr.colorBackground);
    }

    public static int getLibCopyrightBackgroundRes(Context context) {
        return getColorRes(context, R.attr.colorLibCopyrightBackground);
    }

    public static int getImagePlaceHolderColorRes(Context context) {
        return getColorRes(context, R.attr.colorImagePlaceHolder);
    }

    public static int getCardColorRes(Context context) {
        return getColorRes(context, R.attr.colorCard);
    }

    private static int getColorRes(Context context, int attrId) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.resourceId;
    }
}
