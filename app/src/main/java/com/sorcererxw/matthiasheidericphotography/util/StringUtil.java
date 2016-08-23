package com.sorcererxw.matthiasheidericphotography.util;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class StringUtil {
    public static String handleProjectName(String name) {
        String[] sa = name.split("-");
        String res = "";
        for (String aSa : sa) {
            res += aSa.substring(0, 1).toUpperCase() + aSa.substring(1) + " ";
        }
        return res;
    }

    public static String onlyLetter(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                res+=s.charAt(i);
            }
        }
        return res;
    }
}
