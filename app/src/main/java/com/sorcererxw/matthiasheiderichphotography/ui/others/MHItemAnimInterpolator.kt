package com.sorcererxw.matthiasheiderichphotography.ui.others


import android.view.animation.Interpolator

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/15
 */

class MHItemAnimInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return Math.pow(input.toDouble(), 4.0).toFloat()
    }
}
