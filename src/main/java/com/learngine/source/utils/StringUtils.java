package com.learngine.source.utils;

public abstract class StringUtils {
    public static String removeExtraWhitespaces(String strToClean) {
        return strToClean.trim().replaceAll("\\s+", " ");
    }
}
