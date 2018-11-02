package com.sorcererxw.matthiasheiderichphotography.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryDao
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoDao
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
@Database(entities = [
    PhotoEntity::class,
    GalleryEntity::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
    abstract fun photoDao(): PhotoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(applicationContext: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDb(applicationContext)
            }
            return INSTANCE!!
        }

        private fun buildDb(applicationContext: Context) = Room
                .databaseBuilder(applicationContext, AppDatabase::class.java, "app_db")
                .allowMainThreadQueries()
                .build()
    }
}