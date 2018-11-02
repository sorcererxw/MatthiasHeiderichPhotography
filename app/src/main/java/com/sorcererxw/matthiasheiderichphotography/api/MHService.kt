package com.sorcererxw.matthiasheiderichphotography.api

import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @description:
 * @author: Sorcerer
 * @date: 11/1/2018
 */
interface MHService {
    companion object {
        const val BASE_URL = "https://api.mh.sorcererxw.com/v1/"
    }

    @GET("gallery")
    fun galleryList(): Observable<List<GalleryEntity>>

    @GET("photo")
    fun photoList(@Query("gallery") gallery: String): Observable<List<PhotoEntity>>
}