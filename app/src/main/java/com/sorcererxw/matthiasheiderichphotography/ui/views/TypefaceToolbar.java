package com.sorcererxw.matthiasheiderichphotography.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/24
 */
public class TypefaceToolbar extends Toolbar {
    public TypefaceToolbar(Context context) {
        super(context);
        init(context);
    }

    public TypefaceToolbar(Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TypefaceToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private TextView mTitleTextView;

    private void init(Context context) {
        mTitleTextView = new TextView(context);
        mTitleTextView.setTextColor(ResourceUtil.getColor(context, R.color.colorPrimaryText));
        mTitleTextView.setTextSize(20);
        mTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mTitleTextView.setTypeface(
                TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Demi));
        addView(mTitleTextView);
        super.setTitle("");
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        mTitleTextView.setText(title);
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        super.setTitleTextColor(color);
        mTitleTextView.setTextColor(color);
    }
}
