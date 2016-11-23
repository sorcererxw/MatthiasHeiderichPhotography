package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import com.sorcererxw.matthiasheiderichphotography.util.Prefs;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.typefaceviews.TypefaceViews;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        TypefaceViews.install(
                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Demi),
                TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book)
        );
        mPrefs = new Prefs(this);
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
