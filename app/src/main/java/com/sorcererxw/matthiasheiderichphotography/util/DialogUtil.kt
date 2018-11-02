package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import android.view.View
import android.widget.TextView

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs
import com.sorcererxw.matthiasheidericphotography.R
import com.wang.avi.AVLoadingIndicatorView

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/24
 */
object DialogUtil {
    fun getProgressDialog(context: Context, message: String): MaterialDialog {
        val view = View.inflate(context, R.layout.layout_progressdialog, null)
        val textView = view.findViewById(R.id.textView_progress_message) as TextView
        textView.text = message
//        textView.setTextColor(ThemeHelper.getSecondaryTextColor(context))
        textView.typeface = TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book)

        val indicator = view.findViewById(
                R.id.loadingIndicator_progress_dialog) as AVLoadingIndicatorView
//        indicator.setIndicatorColor(ThemeHelper.getAccentColor(context))
        return MaterialDialog(context)
                .customView(view=view)
//                .customView(view, true)
                .cancelable(false)
                .cancelOnTouchOutside(false)
    }
}
