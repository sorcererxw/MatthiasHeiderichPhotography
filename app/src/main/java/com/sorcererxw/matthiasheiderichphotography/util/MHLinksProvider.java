package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;

import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.db.ProjectDBHelper;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/17
 */

public class MHLinksProvider {
    private Context mContext;
    private String mProjectName;

    private ProjectDBHelper mDBHelper;

    public MHLinksProvider(Context context, String projectName) {
        mContext = context;
        mProjectName = projectName;
    }

    public Observable<List<String>> getLink() {
        mDBHelper = new ProjectDBHelper(mContext, StringUtil.onlyLetter(mProjectName));
        return getLinks_().doOnNext(new Action1<List<String>>() {
            @Override
            public void call(List<String> list) {
                mDBHelper.close();
            }
        });
    }

    private Observable<List<String>> getLinks_() {
        final MHPreference<Long> lastSync =
                MHApp.getInstance().getPrefs().getLastSync(mProjectName, 0L);

        if (System.currentTimeMillis() - lastSync.getValue() < 86400000) {
            List<String> list = mDBHelper.getLinks();
            if (list.size() == 0) {
                lastSync.setValue(0L);
                return getLinks_();
            } else {
                return Observable.just(list).doOnNext(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        mDBHelper.close();
                    }
                });
            }
        }
        return WebCatcher.catchImageLinks("http://www.matthias-heiderich.de/" + mProjectName)
                .doOnNext(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        lastSync.setValue(System.currentTimeMillis());
                        mDBHelper.saveLinks(list);
                    }
                });
    }
}
