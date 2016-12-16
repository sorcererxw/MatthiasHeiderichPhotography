package com.sorcererxw.typefaceviews;

import android.graphics.Typeface;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

public class TypefaceViews {
    public enum TypefaceType {
        MEDIUM,
        REGULAR
    }

    private static Typeface mMediumTypeface = null;
    private static Typeface mRegularTypeface = null;

    public static void install(Typeface mediumTypeface,
                               Typeface regularTypeface) {
        mMediumTypeface = mediumTypeface;
        mRegularTypeface = regularTypeface;
    }

    public static Typeface getMediumTypeface() {
        return mMediumTypeface;
    }

    public static Typeface getRegularTypeface() {
        return mRegularTypeface;
    }
}
