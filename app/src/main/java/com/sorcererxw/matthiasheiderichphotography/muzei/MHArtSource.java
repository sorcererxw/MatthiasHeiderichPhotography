package com.sorcererxw.matthiasheiderichphotography.muzei;

import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/12
 */

public class MHArtSource extends RemoteMuzeiArtSource {

    private static final String SOURCE_NAME = "MatthiasHeiderichArtSource";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000;

    public MHArtSource() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(ROTATE_TIME_MILLIS);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse("https://static1.squarespace.com/static/53711ac5e4b02c5d204b1870/538c3b9ae4b069dee89ba72c/538c41b0e4b010ba101f082f/1401700787443/MHeiderich_ColorBerlin_01.jpg"))
                .build());

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }
}
