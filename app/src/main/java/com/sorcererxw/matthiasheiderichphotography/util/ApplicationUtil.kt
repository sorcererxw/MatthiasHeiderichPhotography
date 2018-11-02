package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import android.content.pm.PackageManager

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

object ApplicationUtil {
    fun isApplicationInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
