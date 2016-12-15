package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcererxw.matthiasheidericphotography.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

public class FilePickerAdapter extends Adapter<FilePickerAdapter.FilePickerViewHolder> {

    private Context mContext;

    private List<File> mFileList = new ArrayList<>();

    private File mBasePath;

    private File mCurrentPath;

    public FilePickerAdapter(Context context, File basePath) {
        mContext = context;
        mBasePath = basePath;
        setPath(basePath);
    }

    private void setPath(File path) {
        Timber.d(path.getAbsolutePath());
        if (!path.isDirectory()) {
            Timber.d("no path");
            return;
        }
        if (path.equals(mCurrentPath)) {
            return;
        }
        mCurrentPath = path;
        mFileList = Arrays.asList(path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        }));
        notifyDataSetChanged();
    }

    @Override
    public FilePickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilePickerViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_file_picker, parent, false));
    }

    @Override
    public void onBindViewHolder(FilePickerViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setText("\\");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        setPath(mCurrentPath.getParentFile());
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }
            });
        } else {
            final File file = mFileList.get(position - 1);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPath(file);
                }
            });

            holder.title.setText(file.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mFileList.size() + 1;
    }

    static class FilePickerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_item_file_picker_icon)
        ImageView icon;

        @BindView(R.id.textView_item_file_picker_title)
        TextView title;

        public FilePickerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}