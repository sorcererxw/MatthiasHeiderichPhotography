package com.sorcererxw.matthiasheiderichphotography.util;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class StringUtil {

    public static boolean isOnlyLetter(String s, Character c) {
        boolean res = true;
        for (int i = 0; i < s.length(); i++) {
            res &= c.equals(s.charAt(i));
        }
        return res;
    }

    public static String onlyLetter(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                res += s.charAt(i);
            }
        }
        return res;
    }

    public static String getFileNameFromLinkWithoutExtension(String link) {
        String[] sa = link.split("/");
        return sa[sa.length - 1].split("\\.")[0];
    }

    public static String resolutionToString(int resolution) {
        if (resolution <= 1080) {
            return resolution + "p";
        }
        if (resolution / 1000 == 2) {
            return "2K";
        }
        if (resolution / 1000 == 4) {
            return "4K";
        }
        return "";
    }
}
