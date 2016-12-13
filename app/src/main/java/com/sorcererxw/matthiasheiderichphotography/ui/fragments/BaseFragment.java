package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/31
 */
public abstract class BaseFragment extends Fragment {
    public void onShow() {

    }

    public void onToolbarDoubleTap() {

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    public void onThemeChanged() {
        refreshUI();
    }

    protected void refreshUI() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = provideContentView(inflater, container, savedInstanceState);
        return view;
    }

    protected abstract View provideContentView(LayoutInflater inflater,
                                               @Nullable ViewGroup container,
                                               @Nullable Bundle savedInstanceState);
}
