package com.sorcererxw.matthiasheiderichphotography.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.sorcererxw.matthiasheiderichphotography.MHApp

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        MHApp.instance!!.scheduleManager!!.startAutoChangeWallpaperIfNeeded()
    }
}