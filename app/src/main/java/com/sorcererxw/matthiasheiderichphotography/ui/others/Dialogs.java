package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.app.Activity;
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

    public static TransparentStatusBarBottomSheetDialog FilePickerBottomSheetDialog(
            Activity activity, CharSequence title,
            FilePickerSheetView.OnFileSelectedCallBack callBack) {
        TransparentStatusBarBottomSheetDialog dialog =
                new TransparentStatusBarBottomSheetDialog(activity);
        FilePickerSheetView sheetView = new FilePickerSheetView(activity);
        sheetView.setTitle(title);
        sheetView.setOnFileSelectedCallBack(callBack);
        sheetView.setIsAutoDismiss(dialog, true);
        dialog.setContentView(sheetView);
        return dialog;
    }
}
