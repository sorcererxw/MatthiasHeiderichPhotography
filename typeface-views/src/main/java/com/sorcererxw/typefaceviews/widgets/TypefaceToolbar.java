package com.sorcererxw.typefaceviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

public class TypefaceToolbar extends Toolbar {
    public TypefaceToolbar(Context context) {
        super(context);
        init();
    }

    public TypefaceToolbar(Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Typeface mTitleTypeface = TypefaceViews.getMediumTypeface();
    private Typeface mSubtitleTypeface = TypefaceViews.getRegularTypeface();

    private void init() {
        if (mHasText) {
            if (getTitle() != null && getTitle().length() > 0) {
                setTitle(getTitle());
            }
            if (getSubtitle() != null && getSubtitle().length() > 0) {
                setSubtitle(getSubtitle());
            }
        }
    }

    private boolean mHasText = true;

    public void setHasText(boolean hasText) {
        mHasText = hasText;
        if (mHasText) {
            setTitle(getTitle());
            setSubtitle(getSubtitle());
        } else {
            setTitle("");
            setSubtitle("");
        }
    }

    public void setTitleTypeface(TypefaceViews.TypefaceType type) {
        if (type == TypefaceViews.TypefaceType.MEDIUM) {
            setTitleTypeface(TypefaceViews.getMediumTypeface());
        } else if (type == TypefaceViews.TypefaceType.REGULAR) {
            setTitleTypeface(TypefaceViews.getRegularTypeface());
        }
    }

    public void setTitleTypeface(Typeface typeface) {
        mTitleTypeface = typeface;
        refreshTitleTypeface();
    }

    public void setSubtitleTypeface(Typeface typeface) {
        mSubtitleTypeface = typeface;
        refreshSubtitleTypeface();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        refreshTitleTypeface();
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        super.setTitle(subtitle);
        refreshSubtitleTypeface();
    }

    private void refreshTitleTypeface() {
        try {
            Class<?> clazz = getClass().getSuperclass();
            Field field = clazz.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            TextView textView = (TextView) field.get(this);
            if (textView != null) {
                textView.setTypeface(mTitleTypeface);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void refreshSubtitleTypeface() {
        try {
            Class<?> clazz = getClass().getSuperclass();
            Field field = clazz.getDeclaredField("mSubtitleTextView");
            field.setAccessible(true);
            TextView textView = (TextView) field.get(this);
            if (textView != null) {
                textView.setTypeface(mSubtitleTypeface);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
