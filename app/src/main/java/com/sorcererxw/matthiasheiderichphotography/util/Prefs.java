package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class Prefs {
    private final SharedPreferences mSharedPreferences;

    public Prefs(Context context) {
        mSharedPreferences =
                context.getSharedPreferences("matthiasheiderich", Context.MODE_PRIVATE);
    }

    public MHPreference<Long> getLastSync(String projectName, Long defaultValue) {
        return new MHPreference<>(mSharedPreferences, "last_sync_" + projectName, defaultValue);
    }

    public static final String KEY_MUZEI_ROTATE_TIME = "KEY_MUZEI_ROTATE_TIME";
    public static final String KEY_MUZEI_ROTATE_CATEGORY = "KEY_MUZEI_ROTATE_CATEGORY";
    public static final String KEY_MUZEI_ROTATE_ONLY_WIFI = "KEY_MUZEI_ROTATE_ONLY_WIFI";

    private static final String KEY_LAST_LEAVE_FRAGMENT_TAG = "KEY_LAST_LEAVE_FRAGMENT_TAG";

    public static final String KEY_THEME_NIGHT_MODE = "KEY_THEME_NIGHT_MODE";

    public MHPreference<Long> getMuzeiRotateTime() {
        return new MHPreference<>(mSharedPreferences, KEY_MUZEI_ROTATE_TIME, 1000 * 60 * 60 * 24L);
    }

    public MHPreference<String> getMuzeiRotateCategory() {
        return new MHPreference<>(mSharedPreferences, KEY_MUZEI_ROTATE_CATEGORY, "");
    }

    public MHPreference<Boolean> getMuzeiRotateOnlyWifi() {
        return new MHPreference<>(mSharedPreferences, KEY_MUZEI_ROTATE_ONLY_WIFI, true);
    }

    public MHPreference<String> getLastLeaveFragmentTag() {
        return new MHPreference<>(mSharedPreferences, KEY_LAST_LEAVE_FRAGMENT_TAG,
                MainActivity.FRAGMENT_TAG_HOME);
    }

    public MHPreference<Boolean> getThemeNightMode() {
        return new MHPreference<>(mSharedPreferences, KEY_THEME_NIGHT_MODE, false);
    }

    public static final String KEY_AUTO_ROTATE_TIME = "KEY_AUTO_ROTATE_TIME";
    public static final String KEY_AUTO_ROTATE_ENABLE = "KEY_AUTO_ROTATE_ENABLE";
    public static final String KEY_AUTO_ROTATE_WIFI = "KEY_AUTO_ROTATE_WIFI";

    public MHPreference<Long> getAutoRotateTime() {
        return new MHPreference<>(mSharedPreferences, KEY_AUTO_ROTATE_TIME, 1000 * 60 * 60 * 24L);
    }

    public MHPreference<Boolean> getAutoRotateEnable() {
        return new MHPreference<>(mSharedPreferences, KEY_AUTO_ROTATE_ENABLE, false);
    }

    public MHPreference<Boolean> getAutoRotateOnlyInWifi() {
        return new MHPreference<>(mSharedPreferences, KEY_AUTO_ROTATE_WIFI, false);
    }
}
