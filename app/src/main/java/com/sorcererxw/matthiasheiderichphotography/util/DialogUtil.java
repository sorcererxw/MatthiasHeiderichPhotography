package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs;
import com.sorcererxw.matthiasheidericphotography.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/24
 */
public class DialogUtil {
    public static MaterialDialog getProgressDialog(Context context, String message) {
        View view = View.inflate(context, R.layout.layout_progressdialog, null);
        TextView textView = (TextView) view.findViewById(R.id.textView_progress_message);
        textView.setText(message);
        textView.setTextColor(
                ResourceUtil.getColor(context, StyleUtil.getSecondaryTextColor(context)));
        textView.setTypeface(TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book));

        AVLoadingIndicatorView indicator =
                (AVLoadingIndicatorView) view.findViewById(R.id.loadingIndicator_progress_dialog);
        indicator.setIndicatorColor(
                ResourceUtil.getColor(context, StyleUtil.getAccentColorRes(context)));
        return Dialogs.TypefaceMaterialDialogBuilder(context)
                .customView(view, true)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
    }
}
