package com.sorcererxw.matthiasheidericphotography

import com.sorcererxw.matthiasheiderichphotography.util.StringUtil

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.junit.Test


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class ExampleUnitTest {

    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        val document = Jsoup.connect("http://www.matthias-heiderich.de/material-i/")
                .userAgent(USER_AGENT)
                .get()
        val elements = document.getElementsByClass("slideshow stacked")[0]
                .getElementsByClass("slide")
        println(elements.size)
        for (element in elements) {
            println(element.select("img.loading").attr("data-src"))
        }
    }

    companion object {

        private val USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)"
    }
}