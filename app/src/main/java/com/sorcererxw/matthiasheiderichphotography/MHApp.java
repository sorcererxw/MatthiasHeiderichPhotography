package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class MHApp extends Application {
    public static String[] PROJECTS_NAME = new String[]{
            "material-i",
            "reflections-1",
            "nowhere-in-particular",
            "systems-layers-iii",
            "systems-layers-ii",
            "systems-layers",
            "northbound",
            "reflexionen-drei",
            "reflexionen-zwei",
            "reflexionen-eins",
            "reflexiones",
            "spektrum-eins",
            "spektrum-zwei",
            "fragment",
            "uae",
            "stadt-der-zukunft",
            "kali",
            "a7-southbound",
            "ost-west",
            "studien",
            "color-berlin",
            "random"
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

//        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
//        previewResolution = sharedPreferences.getInt(PREFERENCE_SETTINGS_PREVIEW_RESOLUTION, 1080);
//        downloadResolution =
//                sharedPreferences.getInt(PREFERENCE_SETTINGS_DOWNLOAD_RESOLUTION, 1080);
    }

    private static MHApp mApp;

    public static MHApp getInstance() {
        return mApp;
    }

    private Drawable mTmpDrawable;

    public void setTmpDrawable(Drawable drawable) {
        mTmpDrawable = drawable;
    }

    public Drawable getTmpDrawable() {
        return mTmpDrawable;
    }

    public static final int[] RESOLUTIONS = new int[]{
            240, 480, 720, 1080, 2160
    };

    public static final String PREFERENCE_SETTINGS_PREVIEW_RESOLUTION = "preview_resolution";
    public static final String PREFERENCE_SETTINGS_DOWNLOAD_RESOLUTION = "download_resolution";

    public static int previewResolution;
    public static int downloadResolution;
}
