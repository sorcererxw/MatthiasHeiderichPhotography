package com.sorcererxw.matthiasheiderichphotography.room.gallery

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery ORDER BY name")
    fun galleryList(): LiveData<List<GalleryEntity>>

    @Query("SELECT * FROM gallery WHERE name=:name LIMIT 1")
    fun getGallery(name: String): LiveData<GalleryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<GalleryEntity>)
}
