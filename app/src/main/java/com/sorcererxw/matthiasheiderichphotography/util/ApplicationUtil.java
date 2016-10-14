package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class ApplicationUtil {
    public static boolean isApplicationInstalled(Context context,String packageName){
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
