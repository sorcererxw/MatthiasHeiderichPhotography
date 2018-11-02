package com.sorcererxw.matthiasheiderichphotography.util

import androidx.core.util.Pair

import java.util.ArrayList
import java.util.Objects

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/14
 */

object ListUtil {

    fun <T, E> findInPairListByFirst(pairList: List<Pair<T, E>>, first: T): Pair<T, E>? {
        for (pair in pairList) {
            if (pair.first == first) {
                return pair
            }
        }
        return null
    }

    fun <T, E> findInPairListBySecond(pairList: List<Pair<T, E>>, second: E): Pair<T, E>? {
        for (pair in pairList) {
            if (pair.second == second) {
                return pair
            }
        }
        return null
    }

}
