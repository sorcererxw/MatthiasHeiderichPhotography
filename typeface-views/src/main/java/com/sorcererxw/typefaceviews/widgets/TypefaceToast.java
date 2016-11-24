package com.sorcererxw.typefaceviews;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

public class TypefaceToast extends Toast {

    private Typeface mTypeface = TypefaceViews.getRegularTypeface();

    public TypefaceToast(Context context) {
        super(context);
    }

    public void setTextTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    @Override
    public void show() {
        LinearLayout toastLayout = (LinearLayout) getView();
        if (toastLayout != null) {
            TextView textView = (TextView) toastLayout.getChildAt(0);
            textView.setTypeface(mTypeface);
        }
        super.show();
    }
}
