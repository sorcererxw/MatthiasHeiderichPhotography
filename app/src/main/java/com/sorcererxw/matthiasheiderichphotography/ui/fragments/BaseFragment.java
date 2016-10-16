package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.support.v4.app.Fragment;

import com.sorcererxw.matthiasheiderichphotography.MHApp;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/31
 */
public class BaseFragment extends Fragment {
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
}
