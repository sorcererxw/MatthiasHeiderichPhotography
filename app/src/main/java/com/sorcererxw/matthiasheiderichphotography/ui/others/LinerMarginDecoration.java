package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sorcerer on 2016/8/23.
 */
public class LinerMarginDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public LinerMarginDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
    }
}
