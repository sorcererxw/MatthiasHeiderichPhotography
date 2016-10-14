package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.sorcererxw.matthiasheiderichphotography.util.Prefs;

import okhttp3.OkHttpClient;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
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

    private Prefs mPrefs;

    public Prefs getPrefs() {
        return mPrefs;
    }

    private OkHttpClient mOkHttpClient;

    public OkHttpClient getHttpClient() {
        return mOkHttpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        mPrefs = new Prefs(this);

        mOkHttpClient = new OkHttpClient.Builder().build();
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

}
