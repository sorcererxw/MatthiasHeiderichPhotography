package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.support.v4.app.Fragment;

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

    private float mInitX;
    private float mInitY;
    private float mLastX;
    private float mLastY;
    private boolean mIsToOpen;
    private long mDownTime;

    @Override
    public void onResume() {
        super.onResume();
//        View view = getView();
//        if (view != null) {
//            Toast.makeText(getContext(), "touch add", Toast.LENGTH_SHORT).show();
//            view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    float x = event.getX();
//                    float y = event.getY();
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            mInitX = x;
//                            mInitY = y;
//                            mLastX = x;
//                            mLastY = y;
//                            mIsToOpen = true;
//                            mDownTime = System.currentTimeMillis();
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            if (mIsToOpen && Math.abs(x - mInitX) > Math.abs(y - mInitY)
//                                    && (x - mInitX) > 300
//                                    && System.currentTimeMillis() - mDownTime <= 500) {
//                                ((MainActivity) getActivity()).openDrawer();
//                            }
//                            break;
//                    }
//                    return false;
//                }
//            });
//        }
    }
}
