package com.sorcererxw.matthiasheiderichphotography.muzei;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.android.apps.muzei.api.UserCommand;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.DetailActivity;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;
import com.sorcererxw.matthiasheiderichphotography.util.NetworkUtil;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;

import java.util.List;
import java.util.Random;


import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/12
 */

public class MHArtSource extends RemoteMuzeiArtSource {

    private static final String SOURCE_NAME = "MatthiasHeiderichArtSource";

    public MHArtSource() {
        super(SOURCE_NAME);
    }

    private static final int USER_COMMAND_NEXT_WALLPAPER = 0x1;
    private static final int USER_COMMAND_SETTINGS = 0x2;
    private static final int USER_COMMAND_VIEW_IN_APP = 0x3;


    private static final int REASON_NEXT_WALLPAPER_BY_USER = 0x10;

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(
                new UserCommand(USER_COMMAND_NEXT_WALLPAPER, "Next wallpaper"),
                new UserCommand(USER_COMMAND_SETTINGS, "Settings"),
                new UserCommand(USER_COMMAND_VIEW_IN_APP, "View in application")
        );
    }

    @Override
    protected void onCustomCommand(int id) {
        super.onCustomCommand(id);
        if (id == USER_COMMAND_NEXT_WALLPAPER) {
            try {
                onTryUpdate(REASON_NEXT_WALLPAPER_BY_USER);
            } catch (RetryException e) {
                e.printStackTrace();
            }
        } else if (id == USER_COMMAND_SETTINGS) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment_tag", MainActivity.FRAGMENT_TAG_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == USER_COMMAND_VIEW_IN_APP) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("link", getCurrentArtwork().getToken());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        scheduleUpdate(System.currentTimeMillis()
                + MHApp.getInstance().getPrefs().getMuzeiRotateTime().getValue());
        if (MHApp.getInstance().getPrefs().getMuzeiRotateOnlyWifi().getValue()
                && !NetworkUtil.isWifiConnected(this)
                && reason != REASON_NEXT_WALLPAPER_BY_USER) {
            return;
        }

        final Random random = new Random();
        String category = MHApp.getInstance().getPrefs().getMuzeiRotateCategory().getValue();
        if (category.isEmpty()) {
            category = MHApp.PROJECTS_NAME[random.nextInt(MHApp.PROJECTS_NAME.length)];
        }

        WebCatcher.catchImageLinks("http://www.matthias-heiderich.de/" + category)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        String uri = list.get(random.nextInt(list.size()));

                        publishArtwork(new Artwork.Builder()
                                .title(getName(uri))
                                .token(uri)
                                .byline("Matthias Heiderich")
                                .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                                .imageUri(Uri.parse(uri))
                                .build());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable);
                    }
                });
    }

    public static String getName(String uri) {
        String[] split = uri.split("/");
        String raw = split[split.length - 1];
        return raw.split("\\.")[0];
    }
}
