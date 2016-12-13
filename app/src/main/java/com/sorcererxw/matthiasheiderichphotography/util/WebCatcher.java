package com.sorcererxw.matthiasheiderichphotography.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class WebCatcher {
    private static final String USER_AGENT =
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";

    public static Observable<List<String>> catchImageLinks(String pageAddress) {
        return Observable.just(pageAddress)
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String pageAddress) {
                        List<String> list = new ArrayList<>();
                        try {
                            Document document = Jsoup.connect(pageAddress)
                                    .userAgent(USER_AGENT)
                                    .get();
                            Elements elements =
                                    document.getElementsByClass("slideshow stacked").get(0)
                                            .getElementsByClass("slide");
                            System.out.println(elements.size());
                            for (Element element : elements) {
                                list.add(element.select("img.loading").attr("data-src"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return list;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
