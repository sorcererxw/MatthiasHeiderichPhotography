package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/24
 */
public class TypefaceHelper {
    private static Typeface fontDemi;

    private static Typeface fontBook;

    public enum Type {
        Demi,
        Book
    }

    public static
    @NonNull
    Typeface getTypeface(Context context, Type type) {
        switch (type) {
            case Book:
                if (fontBook == null) {
                    fontBook = Typeface.createFromAsset(context.getAssets(), "FuturaPTBook.otf");
                }
                return fontBook;
            case Demi:
                if (fontDemi == null) {
                    fontDemi = Typeface.createFromAsset(context.getAssets(), "FuturaPTDemi.otf");
                }
                return fontDemi;
        }
        return null;
    }
}
