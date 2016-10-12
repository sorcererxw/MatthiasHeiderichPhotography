package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eftimoff.androipathview.PathView;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.DetailActivity;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheidericphotography.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class MHAdapter extends RecyclerView.Adapter<MHAdapter.MHViewHolder> {

    public static class MHViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pathView_item)
        PathView mPathView;

        @BindView(R.id.imageView_item)
        ImageView image;

        @BindView(R.id.loadingIndicator_item)
        AVLoadingIndicatorView loadingIndicatorView;

        public MHViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public MHAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public MHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MHViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_mh, parent, false));
    }

    private SparseBooleanArray mShowedMap = new SparseBooleanArray();

    public interface OnItemLongClickListener {
        void onLongClick(View view, String data, int position, MHViewHolder holder);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    @Override
    public void onBindViewHolder(final MHViewHolder holder, int position) {
        if (mShowedMap.get(position)) {
            holder.loadingIndicatorView.setVisibility(View.GONE);
        } else {
            holder.loadingIndicatorView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext)
                .load(mList.get(position) + "?format=" + 1000 + "w")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        holder.loadingIndicatorView.setVisibility(View.GONE);
                        mShowedMap.put(holder.getAdapterPosition(), true);
                        return false;
                    }
                })
                .placeholder(new ColorDrawable(ResourceUtil.getColor(mContext, R.color.grey_50)))
                .fitCenter()
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("link", mList.get(holder.getAdapterPosition()));
                if (mShowedMap.get(holder.getAdapterPosition())) {
                    MHApp.getInstance().setTmpDrawable(holder.image.getDrawable());
                } else {
                    MHApp.getInstance().setTmpDrawable(null);
                }
                mContext.startActivity(intent);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onLongClick(view,
                            mList.get(holder.getAdapterPosition()),
                            holder.getAdapterPosition(), holder);
                }
                return true;
            }
        });

    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(
            OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

}
