package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

public class Dialogs {
    private static MaterialDialog.Builder ThemeMaterialDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .backgroundColor(ThemeHelper.getBackgroundColor(context));
    }

    public static MaterialDialog.Builder TypefaceMaterialDialogBuilder(Context context) {
        return ThemeMaterialDialogBuilder(context).typeface(
                TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Demi),
                TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book));
    }
}
