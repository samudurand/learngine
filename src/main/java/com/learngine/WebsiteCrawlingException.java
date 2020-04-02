package com.learngine;

import com.learngine.source.Website;

public class WebsiteCrawlingException extends RuntimeException {

    private String message;

    public WebsiteCrawlingException(Website website, Throwable e) {
        super(e);
        this.message = String.format("An error occurred while crawling through the website [%s]", website.getId());
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
