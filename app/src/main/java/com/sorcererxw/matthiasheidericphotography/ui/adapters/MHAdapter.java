package com.sorcererxw.matthiasheidericphotography.ui.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.MHApplication;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheidericphotography.ui.activities.DetailActivity;
import com.sorcererxw.matthiasheidericphotography.util.ResourceUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class MHAdapter extends RecyclerView.Adapter<MHAdapter.MHViewHolder> {

    class MHViewHolder extends RecyclerView.ViewHolder {
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

    private Map<Integer, Boolean> mShowedMap = new HashMap<>();

    @Override
    public void onBindViewHolder(final MHViewHolder holder, int position) {
        if (mShowedMap.containsKey(holder.getAdapterPosition()) && mShowedMap
                .put(holder.getAdapterPosition(), true)) {
            holder.loadingIndicatorView.setVisibility(View.GONE);
        } else {
            holder.loadingIndicatorView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext)
                .load(mList.get(position) + "?format=1000w")
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
                if (mShowedMap.containsKey(holder.getAdapterPosition()) && mShowedMap
                        .get(holder.getAdapterPosition())) {
                    MHApplication.getInstance().setTmpDrawable(holder.image.getDrawable());
                }else{
                    MHApplication.getInstance().setTmpDrawable(null);
                }
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

}
