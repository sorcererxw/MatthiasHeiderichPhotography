package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;

import java.io.File;

import rx.Observable;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

public class CacheUtil {
    public static Observable<Long> calCacheSize(Context context) {
        return Observable.just(calculateDirectorySize(context.getCacheDir()));
    }

    public static Observable<Boolean> clearCache(Context context) {
        return Observable.just(deleteDirectory(context.getCacheDir()));
    }

    private static long calculateDirectorySize(File directory) {
        long result = 0;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                result += calculateDirectorySize(file);
            } else {
                result += file.length();
            }
        }
        return result;
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return true;
    }
}
