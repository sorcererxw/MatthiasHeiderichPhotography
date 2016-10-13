package com.sorcererxw.matthiasheiderichphotography.muzei;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/12
 */

public class MHArtSource extends RemoteMuzeiArtSource {

    private static final String SOURCE_NAME = "MatthiasHeiderichArtSource";

    private static final int ROTATE_TIME_MILLIS = 10 * 1000;

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
        final Random random = new Random();
        String category = MHApp.PROJECTS_NAME[random.nextInt(MHApp.PROJECTS_NAME.length)];

        WebCatcher.catchImageLinks("http://www.matthias-heiderich.de/" + category)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        String uri = list.get(random.nextInt(list.size()));

                        publishArtwork(new Artwork.Builder()
                                .title(getName(uri))
                                .byline("Matthias Heiderich")
                                .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                                .imageUri(Uri.parse(uri))
                                .build());

                        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    public static String getName(String uri) {
        String[] split = uri.split("/");
        String raw = split[split.length - 1];
        return raw.split("\\.")[0];
    }
}
