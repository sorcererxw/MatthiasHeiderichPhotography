package com.sorcererxw.matthiasheiderichphotography.room.photo

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
interface PhotoDao {
    @Query("SELECT * FROM photo WHERE stared=1 ORDER BY url")
    fun staredList(): LiveData<List<PhotoEntity>>

    @Query("SELECT * FROM photo WHERE gallery=:gallery ORDER BY url")
    fun findByGallery(gallery: String): LiveData<List<PhotoEntity>>

    @Query("SELECT * FROM photo ORDER BY RANDOM() LIMIT 1")
    fun random(): PhotoEntity

    @Query("SELECT * FROM photo WHERE gallery=:gallery ORDER BY RANDOM() LIMIT 1")
    fun random(gallery: String): PhotoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: List<PhotoEntity>)

    @Query("UPDATE photo SET stared=1 WHERE url=:photoUrl")
    fun starPhoto(photoUrl: String)

    @Query("UPDATE photo SET stared=0 WHERE url=:photoUrl")
    fun unstarPhoto(photoUrl: String)

    @Query("SELECT * FROM photo WHERE url=:photoUrl LIMIT 1")
    fun getPhoto(photoUrl: String): LiveData<PhotoEntity>
}