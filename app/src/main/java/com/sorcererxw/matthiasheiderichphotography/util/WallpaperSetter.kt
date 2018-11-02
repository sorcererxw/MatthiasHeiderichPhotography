package com.sorcererxw.matthiasheiderichphotography.util

import android.annotation.TargetApi
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.view.WindowManager

import java.io.File
import java.io.FileInputStream
import java.io.IOException

import timber.log.Timber

import android.app.WallpaperManager.FLAG_LOCK
import android.app.WallpaperManager.FLAG_SYSTEM

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/16
 */

class WallpaperSetter(private val mContext: Context) {
    private val mWallpaperManager: WallpaperManager = WallpaperManager.getInstance(mContext.applicationContext)

    @TargetApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    fun setLockScreenWallpaper(uri: Uri) {
        setLockScreenWallpaper(uriToBitmap(uri))
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    fun setLockScreenWallpaper(bitmap: Bitmap) {
        mWallpaperManager.setBitmap(bitmap, null, true, FLAG_LOCK)
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    fun setSystemWallpaper(uri: Uri) {
        setSystemWallpaper(File(uri.path!!))
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    fun setSystemWallpaper(file: File) {
        mWallpaperManager.setStream(FileInputStream(file), null, true, FLAG_SYSTEM)
    }

    fun setWallpaperSimple(windowManager: WindowManager, uri: Uri) {
        setWallpaperSimple(windowManager, uriToBitmap(uri))
    }


    fun setWallpaperSimple(windowManager: WindowManager, wallPaperBitmap: Bitmap): Boolean {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val screenHeight = size.y

        var width = wallPaperBitmap.width
        width = width * screenHeight / wallPaperBitmap.height
        return try {
            mWallpaperManager.setBitmap(
                    Bitmap.createScaledBitmap(wallPaperBitmap, width, screenHeight, true))
            true
        } catch (e: IOException) {
            Timber.e(e)
            false
        }

    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        val file = File(uri.path!!)
        return BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options())
    }

    private fun cropBitmapFromCenterAndScreenSize(uri: Uri): Bitmap {
        return cropBitmapFromCenterAndScreenSize(uriToBitmap(uri))
    }

    private fun cropBitmapFromCenterAndScreenSize(bitmap: Bitmap): Bitmap {
        var bitmap = bitmap
        val screenWidth: Float
        val screenHeight: Float
        val bitmapWidth = bitmap.width.toFloat()
        val bitmapHeight = bitmap
                .height.toFloat()
        val display = (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
        screenWidth = display.width.toFloat()
        screenHeight = display.height.toFloat()

        val bitmapRatio = bitmapWidth / bitmapHeight
        val screenRatio = screenWidth / screenHeight
        val bitmapNewWidth: Int
        val bitmapNewHeight: Int

        if (screenRatio > bitmapRatio) {
            bitmapNewWidth = screenWidth.toInt()
            bitmapNewHeight = (bitmapNewWidth / bitmapRatio).toInt()
        } else {
            bitmapNewHeight = screenHeight.toInt()
            bitmapNewWidth = (bitmapNewHeight * bitmapRatio).toInt()
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapNewWidth,
                bitmapNewHeight, true)

        val bitmapGapX: Int
        val bitmapGapY: Int
        bitmapGapX = ((bitmapNewWidth - screenWidth) / 2.0f).toInt()
        bitmapGapY = ((bitmapNewHeight - screenHeight) / 2.0f).toInt()

        bitmap = Bitmap.createBitmap(bitmap,
                bitmapGapX, bitmapGapY,
                screenWidth.toInt(), screenHeight.toInt())
        return bitmap
    }

}
