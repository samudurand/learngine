package com.learngine.source.utils;

public abstract class StringUtils {
    public static String removeExtraWhitespaces(String strToClean) {
        return strToClean.trim().replaceAll("\\s+", " ");
    }

    public static String truncate(String str, Integer maxChars) {
        if (str.length() > maxChars) {
            return str.substring(0, maxChars);
        }
        return str;
    }
}
