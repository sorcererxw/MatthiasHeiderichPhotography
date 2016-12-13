package com.sorcererxw.matthiasheiderichphotography;

import com.sorcererxw.matthiasheiderichphotography.ui.fragments.FavoriteFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.HomeFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/13
 */

@Singleton
@Component(modules = MHModule.class)
public interface MHComponent {

    void inject(FavoriteFragment fragment);

    void inject(HomeFragment fragment);

    void inject(MHFragment fragment);

    void inject(SettingsFragment fragment);
}