package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcererxw.matthiasheidericphotography.R;

import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

public class LibAdapter extends RecyclerView.Adapter<LibAdapter.LibViewHolder> {
    static class LibViewHolder extends RecyclerView.ViewHolder {
        public LibViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;

    public LibAdapter(Context context) {
        mContext = context;
    }

    @Override
    public LibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LibViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_library, parent, false));
    }

    @Override
    public void onBindViewHolder(LibViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
