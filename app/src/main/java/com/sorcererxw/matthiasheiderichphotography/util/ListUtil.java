package com.sorcererxw.matthiasheiderichphotography.util;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

public class ListUtil {
    public static <T, E> List<T> getPairFisrtList(List<Pair<T, E>> pairList) {
        List<T> list = new ArrayList<>();
        for (Pair<T, E> pair : pairList) {
            list.add(pair.first);
        }
        return list;
    }

    public static <T, E> List<E> getPairSecondList(List<Pair<T, E>> pairList) {
        List<E> list = new ArrayList<>();
        for (Pair<T, E> pair : pairList) {
            list.add(pair.second);
        }
        return list;
    }

    public static <T, E> Pair<T, E> findInPairListByFirst(List<Pair<T, E>> pairList, T first) {
        for (Pair<T, E> pair : pairList) {
            if (Objects.equals(pair.first, first)) {
                return pair;
            }
        }
        return null;
    }

    public static <T, E> Pair<T, E> findInPairListBySecond(List<Pair<T, E>> pairList, E second) {
        for (Pair<T, E> pair : pairList) {
            if (Objects.equals(pair.second, second)) {
                return pair;
            }
        }
        return null;
    }

    public interface ItemConverter<T, E> {
        E convert(T origin);
    }

    public static <T, E> List<E> convert(List<T> originList, ItemConverter<T, E> converter) {
        List<E> list = new ArrayList<>();
        for (T origin : originList) {
            list.add(converter.convert(origin));
        }
        return list;
    }
}
