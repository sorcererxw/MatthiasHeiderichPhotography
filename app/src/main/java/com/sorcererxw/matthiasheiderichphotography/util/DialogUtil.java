package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheidericphotography.R;

/**
 * Created by Sorcerer on 2016/8/24.
 */
public class DialogUtil {
    public static MaterialDialog getProgressDialog(Context context, String message) {
        View view = View.inflate(context, R.layout.layout_progressdialog, null);
        TextView textView = (TextView) view.findViewById(R.id.textView_progress_message);
        textView.setText(message);
        textView.setTypeface(TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book));
        return new MaterialDialog.Builder(context)
                .customView(view, true)
                .canceledOnTouchOutside(false)
                .build();
    }
}
