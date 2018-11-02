package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.os.Bundle
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceGroup
import androidx.preference.SwitchPreference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems
import com.sorcererxw.matthiasheiderichphotography.MHApp
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BasePreferenceFragment
import com.sorcererxw.matthiasheiderichphotography.util.ApplicationUtil
import com.sorcererxw.matthiasheiderichphotography.util.ListUtil
import com.sorcererxw.matthiasheiderichphotography.viewmodel.SettingsViewmodel
import com.sorcererxw.matthiasheidericphotography.R
import io.reactivex.disposables.CompositeDisposable
import java.util.*

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

class SettingsFragment : BasePreferenceFragment() {
    companion object {
        private const val MUZEI_ROTATE_CATEGORY_ALL = "All Category"
    }

    private val cd = CompositeDisposable()

    private lateinit var model: SettingsViewmodel

    private lateinit var mGeneralClearCacheItem: Preference
    private lateinit var mAutoRotateEnableItem: SwitchPreference
    private lateinit var mAutoRotateTimeItem: Preference
    private lateinit var mAutoRotateWifiItem: SwitchPreference
    private lateinit var mMuzeiPreferenceGroup: PreferenceGroup
    private lateinit var mMuzeiRotateTimeItem: Preference
    private lateinit var mMuzeiRotateCategoryItem: Preference
    private lateinit var mMuzeiRotateOnlyWifiItem: SwitchPreference
    private lateinit var mThemeNightModeItem: SwitchPreference
    private lateinit var aboutItem: Preference

    private val mRotateTimeOptionList = Arrays.asList<Pair<String, Long>>(
            Pair("Every 10 Seconds", 1000 * 10L),
            Pair("Every Minute", 1000 * 60L),
            Pair("Every 10 Minutes", 1000 * 60 * 10L),
            Pair("Every 30 Minutes", 1000 * 60 * 30L),
            Pair("Every Hour", 1000 * 60 * 60L),
            Pair("Every 12 Hours", 1000 * 60 * 60 * 12L),
            Pair("Every Day", 1000 * 60 * 60 * 24L),
            Pair("Every Week", 1000 * 60 * 60 * 24 * 7L)
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        model = ViewModelProviders.of(this).get(SettingsViewmodel::class.java)

        mGeneralClearCacheItem = findPreference("KEY_GENERAL_CLEAR_CACHE")
        mAutoRotateEnableItem = findPreference("KEY_AUTO_ROTATE_ENABLE") as SwitchPreference
        mAutoRotateTimeItem = findPreference("KEY_AUTO_ROTATE_TIME")
        mAutoRotateWifiItem = findPreference("KEY_AUTO_ROTATE_WIFI") as SwitchPreference
        mMuzeiPreferenceGroup = findPreference("KEY_MUZEI_GROUP") as PreferenceGroup
        mMuzeiRotateCategoryItem = findPreference("KEY_MUZEI_ROTATE_CATEGORY")
        mMuzeiRotateTimeItem = findPreference("KEY_MUZEI_ROTATE_TIME")
        mMuzeiRotateOnlyWifiItem = findPreference(
                "KEY_MUZEI_ROTATE_ONLY_WIFI") as SwitchPreference
        mThemeNightModeItem = findPreference("KEY_THEME_NIGHT_MODE") as SwitchPreference
        aboutItem = findPreference("KEY_ABOUT")

        setupGeneral()
        setupAutoRotate()
        setupMuzei()
        setupTheme()

        aboutItem.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            Navigation.findNavController(this@SettingsFragment.view!!)
                    .navigate(SettingsFragmentDirections.ActionSettingsFragmentToAboutFragment())
            true
        }
    }

    private fun setupGeneral() {
        mGeneralClearCacheItem.summary = "..."
        mGeneralClearCacheItem.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            true
        }
    }

    private fun setupAutoRotate() {
        cd.addAll(
                model.appPref.autoRotateEnable.asObservable().subscribe {
                    mAutoRotateEnableItem.isChecked = it
                    mAutoRotateTimeItem.isEnabled = it
                    mAutoRotateWifiItem.isEnabled = it
                },
                model.appPref.autoRotateOnlyInWifi.asObservable().subscribe {
                    mAutoRotateWifiItem.isChecked = it
                }
        )
        mAutoRotateEnableItem.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val value = newValue as Boolean
            model.appPref.autoRotateEnable.set(value)
            if (value) {
                runSchedule(model.appPref.autoRotateTime.get())
            } else {
                MHApp.instance!!.scheduleManager!!.shutdown()
            }
            false
        }

//            val nowAutoRotateTime = mRotateTimeOptionList.firstOrNull { it.second == mAutoRotateTimePreference!!.value }
//            mAutoRotateTimeItem.summary = if (nowAutoRotateTime == null) "${mMuzeiRotateTimePreference!!.value / 1000} S" else nowAutoRotateTime.first
        mAutoRotateTimeItem.setOnPreferenceClickListener {
            MaterialDialog(context).listItems(
                    items = mRotateTimeOptionList.map { pair -> pair.first ?: "" },
                    selection = object : ItemListener {
                        override fun invoke(dialog: MaterialDialog, index: Int,
                                            text: String) {
                            val pair = ListUtil.findInPairListByFirst(
                                    mRotateTimeOptionList,
                                    text)
                            if (pair != null) {
                                model.appPref.autoRotateTime.set(pair.second!!)
                                runSchedule(pair.second!!)
                            }
                        }
                    }).show()
            true
        }

        mAutoRotateWifiItem.setOnPreferenceChangeListener { preference, newValue ->
            model.appPref.autoRotateOnlyInWifi.set(newValue as Boolean)
            true
        }
    }

    private fun setupMuzei() {
        mMuzeiPreferenceGroup.isEnabled = ApplicationUtil
                .isApplicationInstalled(context, "net.nurik.roman.muzei")

//            val nowRotateTime = ListUtil.findInPairListBySecond(mRotateTimeOptionList,
//                    mMuzeiRotateTimePreference!!.value)

        cd.addAll(
                model.appPref.themeNightMode.asObservable().subscribe {
                    mThemeNightModeItem.isChecked = it
                },
                model.appPref.muzeiRotateCategory.asObservable().subscribe {
                    mMuzeiRotateCategoryItem.summary = if (it.isBlank()) MUZEI_ROTATE_CATEGORY_ALL else it
                },
                model.appPref.muzeiRotateTime.asObservable().subscribe {
                    mMuzeiRotateTimeItem.summary = "${it / 1000} S"
                },
                model.appPref.muzeiRotateOnlyWifi.asObservable().subscribe {
                    mMuzeiRotateOnlyWifiItem.isChecked = it
                }
        )
//            mMuzeiRotateTimeItem.summary = if (nowRotateTime == null) ("${mMuzeiRotateTimePreference!!.value / 1000} S") else nowRotateTime.first

        mMuzeiRotateTimeItem.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            MaterialDialog(context).listItems(
                    items = mRotateTimeOptionList.map { pair -> pair.first ?: "" },
                    selection = object : ItemListener {
                        override fun invoke(dialog: MaterialDialog, index: Int, text: String) {
                            val pair = ListUtil.findInPairListByFirst(
                                    mRotateTimeOptionList, text)
                            if (pair != null) {
                                model.appPref.muzeiRotateTime.set(pair.second!!)
                            }
                        }
                    }).show()
            true
        }

        mMuzeiRotateCategoryItem.setOnPreferenceClickListener {
            val items = ArrayList<String>()
            items.add(MUZEI_ROTATE_CATEGORY_ALL)
            items.addAll(model.galleries.value!!.entries.map { entry -> entry.key })
            MaterialDialog(context)
                    .listItems(items = items, selection = object : ItemListener {
                        override fun invoke(dialog: MaterialDialog, index: Int, text: String) {
                            model.appPref.muzeiRotateCategory.set(
                                    if (text == MUZEI_ROTATE_CATEGORY_ALL) ""
                                    else model.galleries.value!![text]!!)
                        }
                    }).show()
            true
        }

        mMuzeiRotateOnlyWifiItem.setOnPreferenceChangeListener { preference, newValue ->
            model.appPref.muzeiRotateOnlyWifi.set(newValue as Boolean)
            true
        }


    }

    private fun setupTheme() {
        cd.addAll(model.appPref.themeNightMode.asObservable().subscribe {
            mThemeNightModeItem.isChecked = it
        })
        mThemeNightModeItem.setOnPreferenceChangeListener { preference, newValue ->
            //            if (newValue as Boolean) {
//                (activity as MainActivity).setTheme(true)
//            } else {
//                (activity as MainActivity).setTheme(false)
//            }
            model.appPref.themeNightMode.set(newValue as Boolean)
            true
        }
    }

    private fun runSchedule(delay: Long) {
        MHApp.instance!!.scheduleManager!!.scheduleChangeWallpaperService(delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        cd.dispose()
    }
}
