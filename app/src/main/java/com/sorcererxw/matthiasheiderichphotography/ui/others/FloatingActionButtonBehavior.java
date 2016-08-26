package com.sorcererxw.matthiasheiderichphotography.ui.others;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;


/**
 * Created by Sorcerer on 2016/8/25.
 */
public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionMenu> {
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionMenu child,
                                   View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionMenu child,
                                          View dependency) {
        if (dependency.getTranslationY() != 0.0) {
            Toast.makeText(parent.getContext(), dependency.getTranslationY() + "",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.animate().translationY(translationY).setDuration(200).start();
        return true;
    }
}
