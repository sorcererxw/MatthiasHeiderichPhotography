package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.FilePickerAdapter;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.TypefaceViews;
import com.sorcererxw.typefaceviews.widgets.TypefaceToolbar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

public class FilePickerSheetView extends FrameLayout {
    public FilePickerSheetView(Context context) {
        super(context);
        init(context);
    }

    public FilePickerSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilePickerSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FilePickerSheetView(Context context, AttributeSet attrs, int defStyleAttr,
                               int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @BindView(R.id.relativeLayout_file_picker_sheet_root)
    RelativeLayout mRoot;

    @BindView(R.id.recyclerView_sheet_file_picker)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar_file_picker_sheet)
    TypefaceToolbar mToolbar;

    private FilePickerAdapter mAdapter;

    private void init(Context context) {
        inflate(context, R.layout.sheet_file_picker, this);
        ButterKnife.bind(this, this);
        mRoot.setBackgroundColor(ThemeHelper.getBackgroundColor(context));
//        mToolbar.setBackgroundColor(ThemeHelper.getPrimaryColor(context));
        mToolbar.setTitleTextColor(ThemeHelper.getPrimaryTextColor(context));

        mAdapter = new FilePickerAdapter(getContext(), Environment.getExternalStorageDirectory());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));

        mToolbar.setTitleTypeface(TypefaceViews.TypefaceType.REGULAR);
        mToolbar.setTitle("Select file...");

        MenuInflater menuInflater = new MenuInflater(context);
        menuInflater.inflate(R.menu.menu_file_picker_sheet, mToolbar.getMenu());
        if (mToolbar.getMenu() != null) {
            MenuItem selectFile = mToolbar.getMenu().findItem(R.id.action_select_file);
            if (selectFile != null) {
                selectFile.setIcon(
                        new IconicsDrawable(context, GoogleMaterial.Icon.gmd_done)
                                .sizeDp(24).color(ThemeHelper.getPrimaryTextColor(context))
                );
                selectFile.setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
                selectFile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (mAdapter != null && mOnFileSelectedCallBack != null) {
                            mOnFileSelectedCallBack.onFileSelected(mAdapter.getCurrentPath());
                            if (mAutoDismiss && mBottomSheetDialog != null) {
                                mBottomSheetDialog.dismiss();
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
        ViewCompat.setElevation(this, DisplayUtil.dip2px(context, 16));
    }

    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    public interface OnFileSelectedCallBack {
        void onFileSelected(File file);
    }

    private OnFileSelectedCallBack mOnFileSelectedCallBack;

    public OnFileSelectedCallBack getOnFileSelectedCallBack() {
        return mOnFileSelectedCallBack;
    }

    public void setOnFileSelectedCallBack(
            OnFileSelectedCallBack onFileSelectedCallBack) {
        mOnFileSelectedCallBack = onFileSelectedCallBack;
    }

    private BottomSheetDialog mBottomSheetDialog = null;
    private boolean mAutoDismiss = false;

    public void setIsAutoDismiss(BottomSheetDialog dialog, boolean autoDismiss) {
        mBottomSheetDialog = dialog;
        mAutoDismiss = autoDismiss;
    }
}
