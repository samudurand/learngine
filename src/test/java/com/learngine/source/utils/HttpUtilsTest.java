package com.learngine.source.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}