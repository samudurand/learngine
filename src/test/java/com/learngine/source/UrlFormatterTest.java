package com.learngine.source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlFormatterTest {

    @Test
    void generateFullLink() {
        var link = UrlFormatter.generateFullLink("http://website.com", "/relative/url");
        assertEquals("http://website.com/relative/url", link);
    }

    @Test
    void generateFullLinkFromBaseUrlWithSlash() {
        var link = UrlFormatter.generateFullLink("http://website.com/", "/relative/url");
        assertEquals("http://website.com/relative/url", link);
    }

    @Test
    void generateFullLinkFromRelativeUrlWithoutSlash() {
        var link = UrlFormatter.generateFullLink("http://website.com", "relative/url");
        assertEquals("http://website.com/relative/url", link);
    }

    @Test
    void generateFullLinkFromFullUrlPassedAsRelative() {
        var link = UrlFormatter.generateFullLink("http://website.com", "http://website.com/relative/url");
        assertEquals("http://website.com/relative/url", link);
    }

    @Test
    void generateFullLinkFromEmptyRelativeUrl() {
        var link = UrlFormatter.generateFullLink("http://website.com", "");
        assertEquals("http://website.com/", link);
    }
}