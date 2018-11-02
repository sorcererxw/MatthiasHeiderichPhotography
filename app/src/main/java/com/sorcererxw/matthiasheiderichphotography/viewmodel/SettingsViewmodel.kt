package com.sorcererxw.matthiasheiderichphotography.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.sorcererxw.matthiasheiderichphotography.room.AppDatabase
import com.sorcererxw.matthiasheiderichphotography.util.AppPref

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
class SettingsViewmodel(application: Application) : AndroidViewModel(application) {
    val appPref = AppPref.getInstance(application)
    private val db = AppDatabase.getInstance(application)

     val galleries: LiveData<Map<String, String>> = Transformations.map(
            db.galleryDao().galleryList()
    ) { list -> list.map { Pair(it.title, it.name) }.toMap() }
}