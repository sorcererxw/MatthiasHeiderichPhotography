package com.sorcererxw.matthiasheidericphotography;

import android.app.Application;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class MHApplication extends Application {
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
    }

    private static MHApplication mApp;

    public static MHApplication getInstance() {
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
