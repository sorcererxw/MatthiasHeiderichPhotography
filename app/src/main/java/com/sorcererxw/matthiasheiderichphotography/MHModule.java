package com.sorcererxw.matthiasheiderichphotography;

import android.app.Application;

import com.sorcererxw.matthiasheiderichphotography.db.DbModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/13
 */
@Module(includes = {
        DbModule.class
})
public class MHModule {
    private final Application mApplication;

    MHModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }
}
