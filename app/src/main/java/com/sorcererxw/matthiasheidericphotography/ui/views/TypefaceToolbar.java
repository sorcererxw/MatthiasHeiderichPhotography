package com.sorcererxw.matthiasheidericphotography.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheidericphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheidericphotography.util.TypefaceHelper;

/**
 * Created by Sorcerer on 2016/8/24.
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

    public void setTypeface(Typeface typeface) {
        mTitleTextView.setTypeface(typeface);
    }
}
