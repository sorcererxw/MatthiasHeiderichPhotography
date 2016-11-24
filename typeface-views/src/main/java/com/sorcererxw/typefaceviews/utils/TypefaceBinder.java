package com.sorcererxw.typefaceviews.util;

import android.graphics.Typeface;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/24
 */

public class TypefaceBinder {
    public static void bindTypeface(TextView textView) {
        bindTypeface(textView, TypefaceViews.getRegularTypeface());
    }

    public static void bindTypeface(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }


}
