package com.sorcererxw.matthiasheiderichphotography.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.sorcererxw.matthiasheidericphotography.R
import timber.log.Timber
import java.io.File
import java.util.*

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

class FilePickerAdapter(private val mContext: Context,
                        private val mBasePath: File) : Adapter<FilePickerAdapter.FilePickerViewHolder>() {

    private var mFileList: List<File> = ArrayList()

    var currentPath: File? = null
        private set

    private val isOnBasePath: Boolean
        get() = currentPath!!.absolutePath == mBasePath.absolutePath

    init {
        setPath(mBasePath)
    }

    private fun setPath(path: File) {
        Timber.d(path.absolutePath)
        if (!path.isDirectory) {
            Timber.d("no path")
            return
        }
        if (path == currentPath) {
            return
        }
        currentPath = path
        mFileList = Arrays.asList(*path.listFiles { pathname -> pathname.isDirectory })
        Collections.sort(mFileList) { o1, o2 -> o1.name.compareTo(o2.name) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilePickerViewHolder {
        return FilePickerViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_file_picker, parent, false))
    }

    override fun onBindViewHolder(holder: FilePickerViewHolder, position: Int) {
        if (!isOnBasePath && position == 0) {
            holder.title.text = "\\"
            holder.itemView.setOnClickListener {
                try {
                    setPath(currentPath!!.parentFile)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        } else {
            val file: File = if (isOnBasePath) {
                mFileList[position]
            } else {
                mFileList[position - 1]
            }

            holder.itemView.setOnClickListener { setPath(file) }

            holder.title.text = file.name
        }

        holder.icon.setImageDrawable(IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_folder)
//                .color(ThemeHelper.getAccentColor(mContext))
                .sizeDp(36))
    }

    override fun getItemCount(): Int {
        return if (isOnBasePath) {
            mFileList.size
        } else mFileList.size + 1
    }

    class FilePickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.imageView_item_file_picker_icon)
        val title: TextView = itemView.findViewById(R.id.textView_item_file_picker_title)
    }
}