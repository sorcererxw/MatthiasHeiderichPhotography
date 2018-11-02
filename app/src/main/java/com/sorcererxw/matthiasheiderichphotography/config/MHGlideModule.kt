package com.sorcererxw.matthiasheiderichphotography.config

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */
@GlideModule
class MHGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
    }
}
