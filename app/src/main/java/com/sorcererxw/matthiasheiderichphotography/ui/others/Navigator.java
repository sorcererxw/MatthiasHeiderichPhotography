package com.sorcererxw.matthiasheiderichphotography.ui.others;

import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/16
 */

public class Navigator {
    // todo: finish navigator
    private Navigator(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    private static Navigator mNavigator;

    private MainActivity mMainActivity;

    public static void init(MainActivity mainActivity) {
        mNavigator = new Navigator(mainActivity);
    }

    public static Navigator getInstance() {
        return mNavigator;
    }

    public void toHomePage() {

    }

    public void toFavoritePage() {

    }

    public void toSettingsPage() {

    }

    public void toCollectionPage(String collectionName) {

    }
}