package com.sorcererxw.matthiasheiderichphotography.ui.views.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class TypefaceMaterialDialogBuilder extends ThemeMaterialDialogBuilder {
    public TypefaceMaterialDialogBuilder(
            @NonNull Context context) {
        super(context);
        typeface(TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Demi),
                TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book));
    }
}
