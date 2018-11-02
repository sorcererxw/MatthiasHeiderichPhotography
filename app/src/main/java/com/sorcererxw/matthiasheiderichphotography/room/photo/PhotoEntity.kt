package com.sorcererxw.matthiasheiderichphotography.room.photo

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/30/2018
 */
@Entity(tableName = "photo", primaryKeys = ["url"])
data class PhotoEntity(
        @SerializedName("url") @ColumnInfo(name = "url") val url: String,
        @SerializedName("gallery") @ColumnInfo(name = "gallery") val gallery: String,
        @ColumnInfo(name = "stared") val stared: Boolean = false
)