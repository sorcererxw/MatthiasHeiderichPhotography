package com.sorcererxw.matthiasheidericphotography

import com.sorcererxw.matthiasheiderichphotography.muzei.MHArtSource
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil

import org.junit.Test

/**
 * Created by Sorcerer on 2016/10/13.
 */

class MHArtSourceUnitTest {
    @Test
    @Throws(Exception::class)
    fun getName_isCorrect() {
        val test = "http://static1.squarespace.com/static/53711ac5e4b02c5d204b1870/5698c644b204d5c6658c87f3/5698c667b204d5c6658c8848/1452852843363/MHeiderich-Reflections_01.jpg"
        println(MHArtSource.getName(test))
    }
}
