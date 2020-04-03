package com.learngine.source.utils;

import com.learngine.exception.EncodingException;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpUtils {
    static public String encodeRequestParams(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Illegal charset used", e);
            return ""; // Cannot happen while using standard charsets
        }
    }

    /**
     * Encode space characters as '%20' instead of '+'
     */
    static public String encodeUrlPathParams(String param) {
        return encodeRequestParams(param).replace("+", "%20");
    }
}
