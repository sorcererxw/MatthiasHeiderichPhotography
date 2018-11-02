package com.sorcererxw.matthiasheiderichphotography.ui.others

import android.app.Activity

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

object Dialogs {
    fun filePickerBottomSheetDialog(
            activity: Activity, title: CharSequence,
            callBack: FilePickerSheetView.OnFileSelectedCallBack
    ): TransparentStatusBarBottomSheetDialog {
        val dialog = TransparentStatusBarBottomSheetDialog(activity)
        val sheetView = FilePickerSheetView(activity)
        sheetView.setTitle(title)
        sheetView.onFileSelectedCallBack = callBack
        sheetView.setIsAutoDismiss(dialog, true)
        dialog.setContentView(sheetView)
        return dialog
    }
}
