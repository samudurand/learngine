package com.learngine.source.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlFormatter {


    public static String generateFullLink(String baseUrl, String absoluteLink) {
        if (absoluteLink.startsWith("http")) {
            log.warn("Full link passed instead of an absolute link, formatting skipped");
            return absoluteLink;
        }

        var baseUrlNoEndSlash = baseUrl.replaceAll("/$", "");
        var relativeLinkNoStartSlash = absoluteLink.replaceAll("^/", "");
        return String.format("%s/%s", baseUrlNoEndSlash, relativeLinkNoStartSlash);
    }
}
