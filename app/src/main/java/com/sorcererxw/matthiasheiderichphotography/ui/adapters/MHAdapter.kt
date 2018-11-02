package com.sorcererxw.matthiasheiderichphotography.ui.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.eftimoff.androipathview.PathView
import com.sorcererxw.matthiasheiderichphotography.config.GlideApp
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity
import com.sorcererxw.matthiasheiderichphotography.ui.others.MHItemAnimInterpolator
import com.sorcererxw.matthiasheidericphotography.R
import java.util.*

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
class MHAdapter(
        private val mContext: Context,
        private var mList: MutableList<PhotoEntity> = ArrayList(),
        var onItemClickListener: OnItemClickListener? = null,
        var onItemLongClickListener: OnItemLongClickListener? = null
) : RecyclerView.Adapter<MHAdapter.MHBaseViewHolder>() {

    private var mNightMode: Boolean = false

    open class MHBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class MHFooterViewHolder(itemView: View) : MHBaseViewHolder(itemView) {
        var footView: PathView = itemView.findViewById(R.id.pathView_foot)
    }

    fun setData(newData: List<PhotoEntity>) {
        val diffResult = DiffUtil.calculateDiff(
                MHDiffCallback(mList, newData),
                true
        )
        mList.clear()
        mList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MHBaseViewHolder {
        return if (viewType == TYPE_FOOTER) {
            MHFooterViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_mh_footer, parent, false))
        } else MHViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_mh, parent, false))
    }

    interface OnItemClickListener {
        fun onLongClick(view: View, data: PhotoEntity, position: Int, holder: MHViewHolder)
    }

    interface OnItemLongClickListener {
        fun onLongClick(view: View, data: PhotoEntity, position: Int, holder: MHViewHolder)
    }

    override fun onBindViewHolder(baseHolder: MHBaseViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            val holder = baseHolder as MHFooterViewHolder
        } else if (getItemViewType(position) == TYPE_GENERAL) {
            val holder = baseHolder as MHViewHolder
//            holder.itemView.setBackgroundColor(ThemeHelper.getImagePlaceHolderColor(mContext))
//
//            val animation = AlphaAnimation(0f, 1f)
//            animation.duration = 500
//            animation.interpolator = MHItemAnimInterpolator()

            val indicator = CircularProgressDrawable(mContext);
            indicator.strokeWidth = 10f
            indicator.strokeCap = Paint.Cap.ROUND
            indicator.centerRadius = 50f

            GlideApp.with(mContext)
                    .asBitmap()
                    .load(mList[position].url)
                    .placeholder(indicator)
                    .centerCrop()
                    .transition(BitmapTransitionOptions().crossFade(200))
                    .into(holder.image)

            holder.itemView.setOnClickListener { view ->
                onItemClickListener?.onLongClick(
                        view,
                        mList[holder.adapterPosition],
                        holder.adapterPosition, holder
                )
            }

            holder.itemView.setOnLongClickListener { view ->
                onItemLongClickListener?.onLongClick(
                        view,
                        mList[holder.adapterPosition],
                        holder.adapterPosition, holder
                )
                true
            }
        }
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setNightMode(isNightMode: Boolean) {
        if (mNightMode == isNightMode) {
            return
        }
        mNightMode = isNightMode
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (mList.size == 0) {
            return 0
        }
        return if (FOOTER_ENABLE) {
            mList.size + 1
        } else mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && FOOTER_ENABLE) {
            TYPE_FOOTER
        } else TYPE_GENERAL
    }

    private class MHDiffCallback(
            private val mOldList: List<PhotoEntity>,
            private val mNewList: List<PhotoEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = mOldList.size

        override fun getNewListSize() = mNewList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldList[oldItemPosition].url == mNewList[newItemPosition].url
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldList[oldItemPosition].url == mNewList[newItemPosition].url
        }
    }

    class MHViewHolder(itemView: View) : MHBaseViewHolder(itemView) {
        internal var drawable: Drawable? = null
        val pathView: PathView = itemView.findViewById(R.id.pathView_item)
        val image: ImageView = itemView.findViewById(R.id.imageView_item)

        fun playLikeAnim(context: Context) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
            pathView.pathAnimator
                    .duration(400)
                    .listenerStart { pathView.alpha = 1f }
                    .listenerEnd {
                        pathView.animate()
                                .setStartDelay(100)
                                .alpha(0f)
                                .setDuration(300)
                                .start()
                    }
                    .interpolator(AccelerateDecelerateInterpolator())
                    .start()
        }

        fun playDislikeAnim(context: Context) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
                    .vibrate(150)
            pathView.animate()
                    .alpha(1f)
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            pathView.animate()
                                    .alpha(0f)
                                    .scaleX(0f)
                                    .scaleY(0f)
                                    .setDuration(500)
                                    .setListener(object : AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation: Animator) {
                                            super.onAnimationEnd(animation)
                                            pathView.scaleX = 1f
                                            pathView.scaleY = 1f
                                        }
                                    })
                                    .start()
                        }
                    })
                    .start()
        }
    }

    companion object {

        private const val FOOTER_ENABLE = false
        private const val TYPE_GENERAL = 0x0
        private const val TYPE_FOOTER = 0x1
    }

}
