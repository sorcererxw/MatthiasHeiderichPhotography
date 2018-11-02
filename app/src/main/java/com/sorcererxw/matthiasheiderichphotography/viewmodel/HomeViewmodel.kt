package com.sorcererxw.matthiasheiderichphotography.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sorcererxw.matthiasheiderichphotography.room.AppDatabase
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
class HomeViewmodel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val galleryList: LiveData<List<GalleryEntity>> = db.galleryDao().galleryList()
    fun saveGalleryList(list: List<GalleryEntity>) {
        db.galleryDao().insert(list)
    }
}