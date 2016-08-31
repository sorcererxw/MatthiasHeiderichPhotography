package com.sorcererxw.matthiasheiderichphotography.ui.views;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * Created by Sorcerer on 2016/8/31.
 */
public class TypefaceSnackbar {
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        TextView textView = (TextView) (snackbar.getView())
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTypeface(TypefaceHelper.getTypeface(snackbar.getView().getContext(),
                TypefaceHelper.Type.Book));
        Button button = (Button) (snackbar.getView())
                .findViewById(android.support.design.R.id.snackbar_action);
        button.setTypeface(TypefaceHelper.getTypeface(snackbar.getView().getContext(),
                TypefaceHelper.Type.Demi));
        return snackbar;
    }
}
