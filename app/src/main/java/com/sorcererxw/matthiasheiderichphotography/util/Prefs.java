package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sorcerer on 2016/10/13.
 */

public class Prefs {
    private final SharedPreferences mSharedPreferences;

    public Prefs(Context context) {
        mSharedPreferences = context.getSharedPreferences("matthiasheiderich", Context.MODE_PRIVATE);
    }

    public MHPreference<Long> getLastSync(String projectName, Long defaultValue) {
        return new MHPreference<>(mSharedPreferences, "last_sync_" + projectName, defaultValue);
    }
}
