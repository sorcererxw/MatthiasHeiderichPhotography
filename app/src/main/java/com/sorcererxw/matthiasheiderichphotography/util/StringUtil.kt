package com.sorcererxw.matthiasheiderichphotography.util

/**
 * Created by Sorcerer on 2016/8/22.
 */
object StringUtil {

    fun isOnlyLetter(s: String, c: Char?): Boolean {
        var res = true
        for (i in 0 until s.length) {
            res = res and (c == s[i])
        }
        return res
    }

    fun onlyLetter(s: String): String {
        var res = ""
        for (i in 0 until s.length) {
            if (Character.isLetter(s[i])) {
                res += s[i]
            }
        }
        return res
    }

    fun getFileNameFromLinkWithoutExtension(link: String): String {
        val sa = link.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return sa[sa.size - 1].split(
                "\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    fun resolutionToString(resolution: Int): String {
        if (resolution <= 1080) {
            return resolution.toString() + "p"
        }
        if (resolution / 1000 == 2) {
            return "2K"
        }
        return if (resolution / 1000 == 4) {
            "4K"
        } else ""
    }
}
