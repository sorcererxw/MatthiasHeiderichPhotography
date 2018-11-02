package com.sorcererxw.matthiasheiderichphotography.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sorcererxw.matthiasheiderichphotography.models.LibraryBean
import com.sorcererxw.matthiasheidericphotography.R

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

class LibAdapter(
        private val mContext: Context,
        private val libraryBeanList: List<LibraryBean>
) : RecyclerView.Adapter<LibAdapter.LibViewHolder>() {

    class LibViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textView_item_library_name)
        val author: TextView = itemView.findViewById(R.id.textView_item_library_author)
        val licence: TextView = itemView.findViewById(R.id.textView_item_library_licence)
        val licenceContainer: FrameLayout = itemView.findViewById(
                R.id.frameLayout_item_library_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibViewHolder {
        return LibViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_library, parent, false))
    }

    override fun onBindViewHolder(holder: LibViewHolder, position: Int) {

//        val colorPrimary = ResourceUtil.getColor(mContext, ThemeHelper.getPrimaryColorRes(mContext))
//        val colorPrimaryText = ResourceUtil.getColor(mContext,
//                ThemeHelper.getPrimaryTextColorRes(mContext))
//        val colorSecondaryText = ResourceUtil.getColor(mContext,
//                ThemeHelper.getSecondaryTextColorRes(mContext))
//        val colorLibCopyrightBackground = ResourceUtil.getColor(mContext,
//                ThemeHelper.getLibCopyrightBackgroundRes(mContext))

//        holder.name.setTextColor(colorSecondaryText)
//        holder.author.setTextColor(colorSecondaryText)
//        holder.licence.setTextColor(colorSecondaryText)
//        holder.licenceContainer.setBackgroundColor(colorLibCopyrightBackground)

        holder.name.text = libraryBeanList[position].name
        holder.author.text = libraryBeanList[position].author
        holder.licence.text = libraryBeanList[position].license
    }

    override fun getItemCount(): Int {
        return libraryBeanList.size
    }
}
