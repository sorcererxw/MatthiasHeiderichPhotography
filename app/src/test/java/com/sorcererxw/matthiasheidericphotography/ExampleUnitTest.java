package com.sorcererxw.matthiasheidericphotography;

import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private static final String USER_AGENT =
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";

    @Test
    public void addition_isCorrect() throws Exception {
        Document document = Jsoup.connect("http://www.matthias-heiderich.de/material-i/")
                .userAgent(USER_AGENT)
                .get();
        Elements elements = document.getElementsByClass("slideshow stacked").get(0)
                .getElementsByClass("slide");
        System.out.println(elements.size());
        for (Element element : elements) {
            System.out.println(element.select("img.loading").attr("data-src"));
        }
    }
}