package com.sorcererxw.matthiasheiderichphotography.ui.others;


import android.view.animation.Interpolator;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

public class MHItemAnimInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return (float) Math.pow(input, 4);
    }
}
