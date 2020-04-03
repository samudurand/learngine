package com.learngine.source.utils;

import com.learngine.config.SearchFailedException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
    static public String encodeSearchParams(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new SearchFailedException(e);
        }
    }

    /**
     * Encode space characters as '%20' instead of '+'
     */
    static public String alternativeEncodeSearchParams(String param) {
        return encodeSearchParams(param).replace("+", "%20");
    }
}
