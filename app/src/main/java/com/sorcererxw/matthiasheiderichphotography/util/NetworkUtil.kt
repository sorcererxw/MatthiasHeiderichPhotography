package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.content.Context.CONNECTIVITY_SERVICE

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

object NetworkUtil {
    fun isWifiConnected(context: Context): Boolean {
        val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        return wifi.isConnected
    }
}
