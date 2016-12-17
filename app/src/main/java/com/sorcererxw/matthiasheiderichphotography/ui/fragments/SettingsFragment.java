package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs;
import com.sorcererxw.matthiasheiderichphotography.util.ApplicationUtil;
import com.sorcererxw.matthiasheiderichphotography.util.CacheUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ListUtil;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.util.Prefs;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.preferences.TypefacePreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class SettingsFragment extends BaseFragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private MHPreferenceFragment mMHPreferenceFragment;

    @Override
    public void onShow() {
        super.onShow();
        if (mMHPreferenceFragment == null) {
            mMHPreferenceFragment = (MHPreferenceFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.fragment_mh_preference);
        }
        mMHPreferenceFragment.onShow();
    }

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    public static class MHPreferenceFragment extends PreferenceFragment {

        private Preference mGeneralClearCacheItem;

        private MHPreference<Boolean> mAutoRotateEnablePreference;
        private SwitchPreference mAutoRotateEnableItem;
        private MHPreference<Long> mAutoRotateTimePreference;
        private Preference mAutoRotateTimeItem;
        private MHPreference<Boolean> mAutoRotateWifiPreference;
        private SwitchPreference mAutoRotateWifiItem;

        private PreferenceGroup mMuzeiPreferenceGroup;

        private MHPreference<Long> mMuzeiRotateTimePreference;
        private Preference mMuzeiRotateTimeItem;

        private MHPreference<String> mMuzeiRotateCategoryPreference;
        private Preference mMuzeiRotateCategoryItem;

        private MHPreference<Boolean> mMuzeiRotateOnlyWifiPreference;
        private SwitchPreference mMuzeiRotateOnlyWifiItem;

        private MHPreference<Boolean> mThemeNightModePreference;
        private SwitchPreference mThemeNightModeItem;

        private List<Pair<String, Long>> mRotateTimeOptionList = Arrays.asList(
                new Pair<>("Every 10 Seconds", 1000 * 10L),
                new Pair<>("Every Minute", 1000 * 60L),
                new Pair<>("Every 10 Minutes", 1000 * 60 * 10L),
                new Pair<>("Every 30 Minutes", 1000 * 60 * 30L),
                new Pair<>("Every Hour", 1000 * 60 * 60L),
                new Pair<>("Every 12 Hours", 1000 * 60 * 60 * 12L),
                new Pair<>("Every Day", 1000 * 60 * 60 * 24L),
                new Pair<>("Every Week", 1000 * 60 * 60 * 24 * 7L)
        );

        private final static String MUZEI_ROTATE_CATEGORY_ALL = "All Category";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            setupGeneral();
            setupAutoRotate();
            setupMuzei();
            setupTheme();
        }

        private void setupGeneral() {
            mGeneralClearCacheItem = findPreference("KEY_GENERAL_CLEAR_CACHE");
            mGeneralClearCacheItem.setSummary("...");
            updateCache(false);
            mGeneralClearCacheItem.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            updateCache(true);
                            return false;
                        }
                    });
        }

        private void setupAutoRotate() {
            mAutoRotateEnablePreference = MHApp.getInstance().getPrefs().getAutoRotateEnable();
            mAutoRotateEnableItem = (SwitchPreference) findPreference("KEY_AUTO_ROTATE_ENABLE");
            mAutoRotateEnableItem.setChecked(mAutoRotateEnablePreference.getValue());
            mAutoRotateEnableItem.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Boolean value = (Boolean) newValue;
                            mAutoRotateTimeItem.setEnabled(value);
                            mAutoRotateWifiItem.setEnabled(value);
                            mAutoRotateEnablePreference.setValue(value);
                            ((SwitchPreference) preference).setChecked(value);
                            if (value) {
                                runSchedule(mAutoRotateTimePreference.getValue());
                            } else {
                                MHApp.getInstance().getScheduleManager().shutdown();
                            }
                            return false;
                        }
                    });

            mAutoRotateTimePreference = MHApp.getInstance().getPrefs().getAutoRotateTime();
            mAutoRotateTimeItem = findPreference("KEY_AUTO_ROTATE_TIME");
            mAutoRotateTimeItem.setEnabled(mAutoRotateEnablePreference.getValue());

            Pair<String, Long> nowAutoRotateTime =
                    ListUtil.findInPairListBySecond(mRotateTimeOptionList,
                            mAutoRotateTimePreference.getValue());
            mAutoRotateTimeItem.setSummary(nowAutoRotateTime == null ?
                    (mMuzeiRotateTimePreference.getValue() / 1000 + "S") :
                    nowAutoRotateTime.first);
            mAutoRotateTimeItem.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Dialogs.TypefaceMaterialDialogBuilder(getActivity())
                                    .items(ListUtil.getPairFirstList(mRotateTimeOptionList))
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog,
                                                                View itemView, int position,
                                                                CharSequence text) {
                                            Pair<String, Long> pair =
                                                    ListUtil.findInPairListByFirst(
                                                            mRotateTimeOptionList,
                                                            text.toString());
                                            if (pair != null) {
                                                mAutoRotateTimePreference
                                                        .setValue(pair.second);
                                                mAutoRotateTimeItem.setSummary(pair.first);
                                                runSchedule(pair.second);
                                            }
                                        }
                                    }).build().show();
                            return true;
                        }
                    });

            mAutoRotateWifiPreference = MHApp.getInstance().getPrefs().getAutoRotateOnlyInWifi();
            mAutoRotateWifiItem = (SwitchPreference) findPreference("KEY_AUTO_ROTATE_WIFI");
            mAutoRotateWifiItem.setChecked(mAutoRotateWifiPreference.getValue());
            mAutoRotateWifiItem.setEnabled(mAutoRotateEnablePreference.getValue());
            mAutoRotateWifiItem.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            Boolean value = (Boolean) newValue;
                            mAutoRotateWifiPreference.setValue(value);
                            ((SwitchPreference) preference).setChecked(value);
                            return false;
                        }
                    });
        }

        private void setupMuzei() {
            mMuzeiPreferenceGroup = (PreferenceGroup) findPreference("KEY_MUZEI_GROUP");
            mMuzeiPreferenceGroup.setEnabled(
                    ApplicationUtil.isApplicationInstalled(getActivity(), "net.nurik.roman.muzei"));

            mMuzeiRotateCategoryPreference =
                    MHApp.getInstance().getPrefs().getMuzeiRotateCategory();
            mMuzeiRotateTimePreference = MHApp.getInstance().getPrefs().getMuzeiRotateTime();
            mMuzeiRotateOnlyWifiPreference =
                    MHApp.getInstance().getPrefs().getMuzeiRotateOnlyWifi();

            mMuzeiRotateCategoryItem = findPreference(Prefs.KEY_MUZEI_ROTATE_CATEGORY);
            mMuzeiRotateTimeItem = findPreference(Prefs.KEY_MUZEI_ROTATE_TIME);
            mMuzeiRotateOnlyWifiItem =
                    (SwitchPreference) findPreference(Prefs.KEY_MUZEI_ROTATE_ONLY_WIFI);

            mMuzeiRotateCategoryItem
                    .setSummary(mMuzeiRotateCategoryPreference.getValue().isEmpty() ?
                            MUZEI_ROTATE_CATEGORY_ALL : mMuzeiRotateCategoryPreference.getValue()
                    );
            Pair<String, Long> nowRotateTime =
                    ListUtil.findInPairListBySecond(mRotateTimeOptionList,
                            mMuzeiRotateTimePreference.getValue());
            mMuzeiRotateTimeItem
                    .setSummary(nowRotateTime == null ?
                            (mMuzeiRotateTimePreference.getValue() / 1000 + "S") :
                            nowRotateTime.first);
            mMuzeiRotateOnlyWifiItem.setChecked(mMuzeiRotateOnlyWifiPreference.getValue());

            mMuzeiRotateTimeItem.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Dialogs.TypefaceMaterialDialogBuilder(getActivity())
                                    .items(ListUtil.getPairFirstList(mRotateTimeOptionList))
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog,
                                                                View itemView, int position,
                                                                CharSequence text) {
                                            Pair<String, Long> pair =
                                                    ListUtil.findInPairListByFirst(
                                                            mRotateTimeOptionList,
                                                            text.toString());
                                            if (pair != null) {
                                                mMuzeiRotateTimePreference.setValue(pair.second);
                                                mMuzeiRotateTimeItem.setSummary(pair.first);
                                            }
                                        }
                                    }).build().show();
                            return true;
                        }
                    });

            mMuzeiRotateCategoryItem.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            List<String> items = new ArrayList<>();
                            items.add(MUZEI_ROTATE_CATEGORY_ALL);
                            items.addAll(Arrays.asList(MHApp.PROJECTS_NAME));
                            Dialogs.TypefaceMaterialDialogBuilder(getActivity()).items(items)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog,
                                                                View itemView, int position,
                                                                CharSequence text) {
                                            mMuzeiRotateCategoryItem
                                                    .setSummary(text.toString());
                                            mMuzeiRotateCategoryPreference.setValue(
                                                    text.equals(MUZEI_ROTATE_CATEGORY_ALL) ?
                                                            "" : text.toString());
                                        }
                                    }).build().show();
                            return true;
                        }
                    });

            mMuzeiRotateOnlyWifiItem.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            mMuzeiRotateOnlyWifiPreference.setValue((Boolean) newValue);
                            return true;
                        }
                    });


        }

        private void setupTheme() {
            mThemeNightModePreference = MHApp.getInstance().getPrefs().getThemeNightMode();
            mThemeNightModeItem = (SwitchPreference) findPreference(Prefs.KEY_THEME_NIGHT_MODE);
            mThemeNightModeItem.setChecked(mThemeNightModePreference.getValue());
            mThemeNightModeItem.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            if ((Boolean) newValue) {
                                ((MainActivity) getActivity()).setTheme(true);
                            } else {
                                ((MainActivity) getActivity()).setTheme(false);
                            }
                            mThemeNightModePreference.setValue((Boolean) newValue);
                            return true;
                        }
                    });
        }

        private void updateCache(final boolean withClearCache) {
            Observable.just(true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean aBoolean) {
                            mGeneralClearCacheItem.setSummary("...");
                            return null;
                        }
                    })
                    .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(Boolean aBoolean) {
                            if (withClearCache) {
                                return CacheUtil.clearCache(getActivity());
                            } else {
                                return Observable.just(aBoolean);
                            }
                        }
                    })
                    .flatMap(new Func1<Boolean, Observable<Long>>() {
                        @Override
                        public Observable<Long> call(Boolean aBoolean) {
                            return CacheUtil.calCacheSize(getActivity());
                        }
                    })
                    .map(new Func1<Long, String>() {
                        @Override
                        public String call(Long longSize) {
                            double size = Double.valueOf(longSize);
                            String unit = "KB";
                            size /= 1024;
                            if (size > 1024) {
                                unit = "MB";
                                size /= 1024;
                            }
                            return String.format("%.2f %s", size, unit);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mGeneralClearCacheItem.setSummary(s);
                        }
                    });
        }

        public void onShow() {
            updateCache(false);
        }

        private void runSchedule(long delay) {
            MHApp.getInstance().getScheduleManager().scheduleChangeWallpaperService(delay);
        }
    }
}
