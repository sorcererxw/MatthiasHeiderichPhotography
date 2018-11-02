package com.sorcererxw.matthiasheiderichphotography.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.sorcererxw.matthiasheiderichphotography.config.GlideApp
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity
import com.sorcererxw.matthiasheidericphotography.R

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
class GalleryAdapter(
        private val context: Context,
        var list: List<GalleryEntity> = emptyList(),
        var onGalleryClickListener: OnGalleryClickListener? = null
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = View.inflate(context, R.layout.item_gallery, null)
        return GalleryViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.itemView.setOnClickListener {
            onGalleryClickListener?.onClick(list[position])
        }
        GlideApp.with(context)
                .asBitmap()
                .load(list[position].cover)
                .centerCrop()
                .transition(BitmapTransitionOptions().crossFade(200))
                .into(holder.cover)
    }

    interface OnGalleryClickListener {
        fun onClick(gallery: GalleryEntity)
    }

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textView_item_gallery_title)
        val cover: ImageView = view.findViewById(R.id.imageView_gallery_cover)
    }
}