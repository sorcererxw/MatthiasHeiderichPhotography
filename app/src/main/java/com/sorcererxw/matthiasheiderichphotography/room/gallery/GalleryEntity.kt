package com.sorcererxw.matthiasheiderichphotography.room.gallery

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
@Entity(tableName = "gallery", primaryKeys = ["name"])
data class GalleryEntity(
        @SerializedName("name") @ColumnInfo(name = "name") val name: String,
        @SerializedName("title") @ColumnInfo(name = "title") val title: String,
        @SerializedName("cover") @ColumnInfo(name = "cover") val cover: String? = null
)