package com.sorcererxw.matthiasheiderichphotography.util

import android.content.Context
import io.reactivex.Observable

import java.io.File


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

object CacheUtil {
    fun calCacheSize(context: Context): Observable<Long> {
        return Observable.just(calculateDirectorySize(context.cacheDir))
    }

    fun clearCache(context: Context): Observable<Boolean> {
        return Observable.just(deleteDirectory(context.cacheDir))
    }

    private fun calculateDirectorySize(directory: File): Long {
        var result: Long = 0
        for (file in directory.listFiles()) {
            if (file.isDirectory) {
                result += calculateDirectorySize(file)
            } else {
                result += file.length()
            }
        }
        return result
    }

    private fun deleteDirectory(directory: File): Boolean {
        if (directory.exists()) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        deleteDirectory(file)
                    } else {
                        file.delete()
                    }
                }
            }
        }
        return true
    }
}
