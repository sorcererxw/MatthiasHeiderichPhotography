package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eftimoff.androipathview.PathView;
import com.mikepenz.materialdrawer.model.BaseViewHolder;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.DetailActivity;
import com.sorcererxw.matthiasheiderichphotography.ui.others.MHItemAnimInterpolator;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.GONE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class MHAdapter extends RecyclerView.Adapter<MHAdapter.MHBaseViewHolder> {

    private static final boolean FOOTER_ENABLE = false;

    public static class MHBaseViewHolder extends RecyclerView.ViewHolder {

        public MHBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class MHFooterViewHolder extends MHBaseViewHolder {

        @BindView(R.id.pathView_foot)
        PathView footView;

        public MHFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MHViewHolder extends MHBaseViewHolder {
        Drawable drawable;

        @BindView(R.id.pathView_item)
        PathView pathView;

        @BindView(R.id.imageView_item)
        ImageView image;

        @BindView(R.id.loadingIndicator_item)
        AVLoadingIndicatorView loadingIndicatorView;

        public MHViewHolder(View itemView) {
            super(itemView);
        }

        public void playLikeAnim(Context context) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(150);
            pathView.getPathAnimator()
                    .duration(400)
                    .listenerStart(new PathView.AnimatorBuilder.ListenerStart() {
                        @Override
                        public void onAnimationStart() {
                            pathView.setAlpha(1);
                        }
                    })
                    .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                        @Override
                        public void onAnimationEnd() {
                            pathView.animate()
                                    .setStartDelay(100)
                                    .alpha(0)
                                    .setDuration(300)
                                    .start();
                        }
                    })
                    .interpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        public void playDislikeAnim(Context context) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(150);
            pathView.animate()
                    .alpha(1)
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            pathView.animate()
                                    .alpha(0)
                                    .scaleX(0)
                                    .scaleY(0)
                                    .setDuration(500)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            pathView.setScaleX(1);
                                            pathView.setScaleY(1);
                                        }
                                    })
                                    .start();
                        }
                    })
                    .start();
        }
    }

    private List<String> mList = new ArrayList<>();
    private Activity mContext;
    private RequestManager mGlideRequestManager;
    private boolean mNightMode;

    public MHAdapter(Activity context) {
        mContext = context;
        mGlideRequestManager = Glide.with(context);
    }

    public void setData(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public MHBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new MHFooterViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_mh_footer, parent, false));
        }
        return new MHViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_mh, parent, false));
    }

    private SparseBooleanArray mShowedMap = new SparseBooleanArray();

    public boolean hasItemShowed(int position) {
        return mShowedMap.get(position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, String data, int position, MHViewHolder holder);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    @Override
    public void onBindViewHolder(MHBaseViewHolder baseHolder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            final MHFooterViewHolder holder = (MHFooterViewHolder) baseHolder;
        } else if (getItemViewType(position) == TYPE_GENERAL) {
            final MHViewHolder holder = (MHViewHolder) baseHolder;
            holder.loadingIndicatorView.setIndicatorColor(ThemeHelper.getAccentColor(mContext));
            holder.itemView.setBackgroundColor(ThemeHelper.getImagePlaceHolderColor(mContext));

            if (mShowedMap.get(position)) {
                holder.loadingIndicatorView.setVisibility(GONE);
            } else {
                holder.loadingIndicatorView.setVisibility(View.VISIBLE);
            }

            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(500);
            animation.setInterpolator(new MHItemAnimInterpolator());

            holder.loadingIndicatorView.setAlpha(1);

            mGlideRequestManager.load(mList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            Timber.e(e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource,
                                                       String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            holder.loadingIndicatorView.animate().alpha(0).start();
                            mShowedMap.put(holder.getAdapterPosition(), true);
                            holder.drawable = resource;
                            return false;
                        }
                    })
                    .animate(animation)
                    .fitCenter()
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("link", mList.get(holder.getAdapterPosition()));
                    if (mShowedMap.get(holder.getAdapterPosition())) {
                        MHApp.setTmpDrawable(mContext, holder.drawable);
                    } else {
                        MHApp.setTmpDrawable(mContext, null);
                    }
                    mContext.startActivityForResult(intent, 0, ActivityOptions
                            .makeSceneTransitionAnimation(mContext, holder.image, "image")
                            .toBundle());
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
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void setNightMode(boolean isNightMode) {
        if (mNightMode == isNightMode) {
            return;
        }
        mNightMode = isNightMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList.size() == 0) {
            return 0;
        }
        if (FOOTER_ENABLE) {
            return mList.size() + 1;
        }
        return mList.size();
    }

    private static final int TYPE_GENERAL = 0x0;
    private static final int TYPE_FOOTER = 0x1;

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && FOOTER_ENABLE) {
            return TYPE_FOOTER;
        }
        return TYPE_GENERAL;
    }

    public void setOnItemLongClickListener(
            OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

}
