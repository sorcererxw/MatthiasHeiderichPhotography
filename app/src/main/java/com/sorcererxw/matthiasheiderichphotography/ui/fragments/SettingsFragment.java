package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
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
import com.sorcererxw.matthiasheiderichphotography.util.ListUtil;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.util.Prefs;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class SettingsFragment extends BaseFragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public static class MHPreferenceFragment extends PreferenceFragment {

        private PreferenceGroup mMuzeiPreferenceGroup;

        private MHPreference<Long> mMuzeiRotateTimePreference;
        private Preference mMuzeiRotateTimeItem;

        private MHPreference<String> mMuzeiRotateCategoryPreference;
        private Preference mMuzeiRotateCategoryItem;

        private MHPreference<Boolean> mMuzeiRotateOnlyWifiPreference;
        private CheckBoxPreference mMuzeiRotateOnlyWifiItem;

        private MHPreference<Boolean> mThemeNightModePreference;
        private SwitchPreference mThemeNightModeItem;

        private List<Pair<String, Long>> mMuzeiRotateTimeOptionList = Arrays.asList(
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

            mMuzeiPreferenceGroup = (PreferenceGroup) findPreference("KEY_MUZEI_GROUP");
            mMuzeiPreferenceGroup.setEnabled(
                    ApplicationUtil.isApplicationInstalled(getActivity(), "net.nurik.roman.muzei"));

            mMuzeiRotateCategoryPreference =
                    MHApp.getInstance().getPrefs().getMuzeiRotateCategory();
            mMuzeiRotateTimePreference = MHApp.getInstance().getPrefs().getMuzeiRotateTime();
            mMuzeiRotateOnlyWifiPreference =
                    MHApp.getInstance().getPrefs().getMuzeiRotateOnlyWifi();
            mThemeNightModePreference = MHApp.getInstance().getPrefs().getThemeNightMode();

            mMuzeiRotateCategoryItem = findPreference(Prefs.KEY_MUZEI_ROTATE_CATEGORY);
            mMuzeiRotateTimeItem = findPreference(Prefs.KEY_MUZEI_ROTATE_TIME);
            mMuzeiRotateOnlyWifiItem =
                    (CheckBoxPreference) findPreference(Prefs.KEY_MUZEI_ROTATE_ONLY_WIFI);
            mThemeNightModeItem = (SwitchPreference) findPreference(Prefs.KEY_THEME_NIGHT_MODE);

            mMuzeiRotateCategoryItem
                    .setSummary(mMuzeiRotateCategoryPreference.getValue().isEmpty() ?
                            MUZEI_ROTATE_CATEGORY_ALL : mMuzeiRotateCategoryPreference.getValue()
                    );
            Pair<String, Long> nowRotateTime =
                    ListUtil.findInPairListBySecond(mMuzeiRotateTimeOptionList,
                            mMuzeiRotateTimePreference.getValue());
            mMuzeiRotateTimeItem
                    .setSummary(nowRotateTime == null ?
                            (mMuzeiRotateTimePreference.getValue() / 1000 + "S") :
                            nowRotateTime.first);
            mMuzeiRotateOnlyWifiItem.setChecked(mMuzeiRotateOnlyWifiPreference.getValue());
            mThemeNightModeItem.setChecked(mThemeNightModePreference.getValue());

            mMuzeiRotateTimeItem.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Dialogs.TypefaceMaterialDialogBuilder(getActivity())
                                    .items(ListUtil.getPairFirstList(mMuzeiRotateTimeOptionList))
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog,
                                                                View itemView, int position,
                                                                CharSequence text) {
                                            Pair<String, Long> pair =
                                                    ListUtil.findInPairListByFirst(
                                                            mMuzeiRotateTimeOptionList,
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
    }
}
