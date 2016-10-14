package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class NetworkUtil {
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();
    }
}
