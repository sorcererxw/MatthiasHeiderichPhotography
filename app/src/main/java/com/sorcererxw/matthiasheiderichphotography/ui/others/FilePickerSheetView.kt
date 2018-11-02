package com.sorcererxw.matthiasheiderichphotography.ui.others

import android.content.Context
import android.os.Environment
import android.util.AttributeSet
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.FilePickerAdapter
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil
import com.sorcererxw.matthiasheidericphotography.R
import java.io.File

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

class FilePickerSheetView : FrameLayout {

    private lateinit var mRoot: RelativeLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar

    private var mAdapter: FilePickerAdapter? = null

    var onFileSelectedCallBack: OnFileSelectedCallBack? = null

    private var mBottomSheetDialog: com.google.android.material.bottomsheet.BottomSheetDialog? = null
    private var mAutoDismiss = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        val view = View.inflate(context, R.layout.sheet_file_picker, null)
        addView(view)
        mRoot = findViewById(R.id.relativeLayout_file_picker_sheet_root)
        mRecyclerView = findViewById(R.id.recyclerView_sheet_file_picker)
        mToolbar = findViewById(R.id.toolbar_file_picker_sheet)

//        mRoot.setBackgroundColor(ThemeHelper.getBackgroundColor(context))
        //        mToolbar.setBackgroundColor(ThemeHelper.getPrimaryColor(context));
//        mToolbar.setTitleTextColor(ThemeHelper.getPrimaryTextColor(context))

        mAdapter = FilePickerAdapter(getContext(), Environment.getExternalStorageDirectory())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false)

//        mToolbar.setTitleTypeface(TypefaceViews.TypefaceType.REGULAR)
        mToolbar.title = "Select file..."

        val menuInflater = MenuInflater(context)
        menuInflater.inflate(R.menu.menu_file_picker_sheet, mToolbar.menu)
        if (mToolbar.menu != null) {
            val selectFile = mToolbar!!.menu.findItem(R.id.action_select_file)
            if (selectFile != null) {
                selectFile.icon = IconicsDrawable(context, GoogleMaterial.Icon.gmd_done)
                        .sizeDp(24)
//                        .color(ThemeHelper.getPrimaryTextColor(context))
                selectFile.setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
                selectFile.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
                    if (mAdapter != null && onFileSelectedCallBack != null) {
                        onFileSelectedCallBack!!.onFileSelected(mAdapter!!.currentPath)
                        if (mAutoDismiss && mBottomSheetDialog != null) {
                            mBottomSheetDialog!!.dismiss()
                        }
                        return@OnMenuItemClickListener true
                    }
                    false
                })
            }
        }
        ViewCompat.setElevation(this, DisplayUtil.dip2px(context, 16f).toFloat())
    }

    fun setTitle(title: CharSequence) {
        mToolbar.title = title
    }

    interface OnFileSelectedCallBack {
        fun onFileSelected(file: File?)
    }

    fun setIsAutoDismiss(dialog: com.google.android.material.bottomsheet.BottomSheetDialog,
                         autoDismiss: Boolean) {
        mBottomSheetDialog = dialog
        mAutoDismiss = autoDismiss
    }
}
