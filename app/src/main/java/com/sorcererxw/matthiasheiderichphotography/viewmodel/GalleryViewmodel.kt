package com.sorcererxw.matthiasheiderichphotography.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sorcererxw.matthiasheiderichphotography.room.AppDatabase
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
class GalleryViewmodel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val staredList: LiveData<List<PhotoEntity>> = db.photoDao().staredList()
    private val photoMap: MutableMap<String, LiveData<List<PhotoEntity>>> = HashMap()
    fun getPhotoList(gallery: String): LiveData<List<PhotoEntity>> {
        if (!photoMap.containsKey(gallery)) {
            photoMap[gallery] = db.photoDao().findByGallery(gallery)
        }
        return photoMap[gallery]!!
    }

    private val galleryMap: MutableMap<String, LiveData<GalleryEntity>> = HashMap()
    fun getGallery(name: String): LiveData<GalleryEntity> {
        if (!galleryMap.containsKey(name)) {
            galleryMap[name] = db.galleryDao().getGallery(name)
        }
        return galleryMap[name]!!
    }

    fun savePhotos(list: List<PhotoEntity>) {
        db.photoDao().insert(list)
    }

    fun starPhoto(photo: String) {
        db.photoDao().starPhoto(photo)
    }

    fun unstarPhoto(photo: String) {
        db.photoDao().unstarPhoto(photo)
    }
}