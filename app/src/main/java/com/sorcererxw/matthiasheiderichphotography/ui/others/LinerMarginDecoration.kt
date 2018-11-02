package com.sorcererxw.matthiasheiderichphotography.ui.others

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Sorcerer on 2016/8/23.
 */
class LinerMarginDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
        outRect.bottom = space
    }
}
