package com.sorcererxw.typefaceviews.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

public class TypefaceButton extends Button {
    public TypefaceButton(Context context) {
        super(context);
        init();
    }

    public TypefaceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setTypeface(TypefaceViews.getRegularTypeface());
    }
}
