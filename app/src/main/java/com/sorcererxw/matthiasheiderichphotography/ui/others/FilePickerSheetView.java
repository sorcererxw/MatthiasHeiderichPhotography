package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sorcererxw.matthiasheiderichphotography.ui.adapters.FilePickerAdapter;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheidericphotography.R;

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

    @BindView(R.id.recyclerView_sheet_file_picker)
    RecyclerView mRecyclerView;

    private FilePickerAdapter mAdapter;

    private void init(Context context) {
        inflate(context, R.layout.sheet_file_picker, this);
        ButterKnife.bind(this, this);
        mAdapter = new FilePickerAdapter(getContext(), Environment.getExternalStorageDirectory());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
        ViewCompat.setElevation(this, DisplayUtil.dip2px(context, 16));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


}
