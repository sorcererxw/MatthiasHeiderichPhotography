package com.sorcererxw.matthiasheidericphotography

import org.junit.Test

/**
 * @description:
 * @author: Sorcerer
 * @date: 11/1/2018
 */
class WebCatchTest {
    @Test
    fun testCatchGallery() {
        WebCatcher.catchGalleries().forEach {
            println(it.toString())
        }
    }

    @Test
    fun testCatchPhoto() {
        WebCatcher.catchGalleries().forEach {
            println(it)
            WebCatcher.catchPhotos(it.name).forEach { photo ->
                println("    $photo")
            }
            println("--------------------")
        }
    }
}