package com.sorcererxw.matthiasheiderichphotography.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class TypefaceMaterialDialogBuilder extends MaterialDialog.Builder {
    public TypefaceMaterialDialogBuilder(
            @NonNull Context context) {
        super(context);
        typeface(TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Demi),
                TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book));
    }
}
