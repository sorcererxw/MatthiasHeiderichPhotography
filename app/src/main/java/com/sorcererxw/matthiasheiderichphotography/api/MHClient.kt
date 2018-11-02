package com.sorcererxw.matthiasheiderichphotography.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @description:
 * @author: Sorcerer
 * @date: 11/1/2018
 */
class MHClient private constructor() {
    companion object {
        private var instance: MHClient? = null
        fun getInstance(): MHClient {
            if (instance == null) {
                instance = MHClient()
            }
            return instance!!
        }
    }

    private val service = createService(MHService.BASE_URL)

    private fun createService(apiAddress: String) = Retrofit.Builder()
            .baseUrl(apiAddress)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                    .connectTimeout(60L, TimeUnit.SECONDS)
                    .readTimeout(60L, TimeUnit.SECONDS)
                    .build())
            .build()
            .create(MHService::class.java)

    fun getGalleryList() = service.galleryList()

    fun getPhotoList(gallery: String) = service.photoList(gallery)
}