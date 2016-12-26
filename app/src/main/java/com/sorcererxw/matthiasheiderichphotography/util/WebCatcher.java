package com.sorcererxw.matthiasheiderichphotography.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/13
 */

public class WebCatcher {
    private static final String USER_AGENT =
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";

    public static Observable<List<String>> catchImageLinks(final String pageAddress) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    List<String> list = new ArrayList<>();
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
                    subscriber.onNext(list);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
