package com.sorcererxw.typefaceviews.widgets;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sorcererxw.typefaceviews.TypefaceViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

@SuppressWarnings("ALL")
public class TypefaceSnackbar {

    public static TypefaceSnackbar make(@NonNull View view,
                                        @NonNull CharSequence text,
                                        int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        TypefaceSnackbar typefaceSnackbar = new TypefaceSnackbar(snackbar);
        typefaceSnackbar.setTextTypeface(TypefaceViews.getRegularTypeface());
        typefaceSnackbar.setActionTypeface(TypefaceViews.getMediumTypeface());
        return typefaceSnackbar;
    }

    private Snackbar mSnackbar;

    private TypefaceSnackbar(Snackbar snackbar) {
        mSnackbar = snackbar;
    }

    public TypefaceSnackbar setTextTypeface(Typeface typeface) {
        TextView textView = (TextView) (mSnackbar.getView())
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTypeface(typeface);
        return this;
    }

    public TypefaceSnackbar setActionTypeface(Typeface typeface) {
        Button button = (Button) (mSnackbar.getView())
                .findViewById(android.support.design.R.id.snackbar_action);
        button.setTypeface(typeface);
        return this;
    }

    public Snackbar getSnackbar() {
        return mSnackbar;
    }

    /**************************************************/
    public TypefaceSnackbar setCallback(Snackbar.Callback callback) {
        mSnackbar.setCallback(callback);
        return this;
    }

    public TypefaceSnackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        mSnackbar.setAction(resId, listener);
        return this;
    }

    public TypefaceSnackbar setAction(CharSequence text, final View.OnClickListener listener) {
        mSnackbar.setAction(text, listener);
        return this;
    }

    @NonNull
    public TypefaceSnackbar setActionTextColor(ColorStateList colors) {
        mSnackbar.setActionTextColor(colors);
        return this;
    }

    @NonNull
    public TypefaceSnackbar setActionTextColor(@ColorInt int color) {
        mSnackbar.setActionTextColor(color);
        return this;
    }

    @NonNull
    public TypefaceSnackbar setText(@NonNull CharSequence message) {
        mSnackbar.setText(message);
        return this;
    }

    @NonNull
    public TypefaceSnackbar setText(@StringRes int resId) {
        mSnackbar.setText(resId);
        return this;
    }

    @NonNull
    public TypefaceSnackbar setDuration(@Snackbar.Duration int duration) {
        mSnackbar.setDuration(duration);
        return this;
    }

    @Snackbar.Duration
    public int getDuration() {
        return mSnackbar.getDuration();
    }

    @NonNull
    public View getView() {
        return mSnackbar.getView();
    }

    public void show() {
        mSnackbar.show();
    }

    public void dismiss() {
        mSnackbar.dismiss();
    }

    public boolean isShown() {
        return mSnackbar.isShown();
    }

    public boolean isShownOrQueued() {
        return mSnackbar.isShownOrQueued();
    }
}
