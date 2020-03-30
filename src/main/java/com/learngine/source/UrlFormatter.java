package com.learngine.source;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UrlFormatter {


    public static String generateFullLink(String baseUrl, String relativeLink) {
        if (relativeLink.startsWith("http")) {
            log.warn("Full link passed as relative link, formatting skipped");
            return relativeLink;
        }

        var baseUrlNoEndSlash = baseUrl.replaceAll("/$", "");
        var relativeLinkNoStartSlash = relativeLink.replaceAll("^/", "");
        return String.format("%s/%s", baseUrlNoEndSlash, relativeLinkNoStartSlash);
    }
}
