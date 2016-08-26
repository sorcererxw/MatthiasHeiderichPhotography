package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.view.Display;

import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;

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

    public static int deviceHeight;

    public static int deviceWidth;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        deviceHeight = DisplayUtil.getScreenHeight(this);
        deviceWidth = DisplayUtil.getScreenWidth(this);
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
