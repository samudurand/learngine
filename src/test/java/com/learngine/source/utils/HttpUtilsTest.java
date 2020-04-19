package com.learngine.source.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpUtilsTest {

    @Test
    public void shouldEncodeRequestParamContainingLettersNumbers() {
        assertEquals("hello123", HttpUtils.encodeRequestParams("hello123"));
    }

    @Test
    public void shouldEncodeRequestParamContainingSpaces() {
        assertEquals("hello+sam++", HttpUtils.encodeRequestParams("hello sam  "));
    }

    @Test
    public void shouldEncodeRequestParamContainingSpecialChars() {
        assertEquals("hello%26sam-%2F", HttpUtils.encodeRequestParams("hello&sam-/"));
    }

    @Test
    public void shouldEncodeUrlParamsContainingSpaces() {
        assertEquals("hello%20sam%20%20", HttpUtils.encodeUrlPathParams("hello sam  "));
    }

    @Test
    public void shouldSanitizeHTMLAndAllowUrls() {
        var imageUrl = "https://i.vodn.in/p-max/200/the-matrix-1967.jpg";
        assertEquals(imageUrl, HttpUtils.sanitizeHTML(imageUrl));
    }

    @Test
    public void shouldSanitizeHTMLByRemovingJSTags() {
        var html = "Some <javascript></javascript>text";
        assertEquals("Some text", HttpUtils.sanitizeHTML(html));
    }

    @Test
    public void shouldSanitizeHTMLByRemovingAllTags() {
        var html = "<div><b>Some</b> <iframe>content</iframe>text</div>";
        assertEquals("Some text", HttpUtils.sanitizeHTML(html));
    }
}