package com.sorcererxw.matthiasheiderichphotography.ui.others

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Sorcerer on 2016/8/25.
 */
class FloatingActionButtonBehavior(
        context: Context,
        attrs: AttributeSet
) : CoordinatorLayout.Behavior<FloatingActionMenu>(context, attrs) {

    override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: FloatingActionMenu,
            dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: FloatingActionMenu,
            dependency: View): Boolean {
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        child.animate().translationY(translationY).setDuration(200).start()
        return true
    }
}
