package com.sorcererxw.matthiasheiderichphotography.ui.views.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.util.StyleUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/16
 */

public class ThemeMaterialDialogBuilder extends MaterialDialog.Builder {
    public ThemeMaterialDialogBuilder(
            @NonNull Context context) {
        super(context);
        backgroundColorRes(StyleUtil.getPrimaryColorRes(context));
    }
}
