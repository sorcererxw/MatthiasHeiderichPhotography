package com.sorcererxw.matthiasheiderichphotography.ui.others

import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/16
 */

class Navigator// todo: finish navigator
private constructor(private val mMainActivity: MainActivity) {

    fun toHomePage() {

    }

    fun toFavoritePage() {

    }

    fun toSettingsPage() {

    }

    fun toCollectionPage(collectionName: String) {

    }

    companion object {

        var instance: Navigator? = null
            private set

        fun init(mainActivity: MainActivity) {
            instance = Navigator(mainActivity)
        }
    }
}