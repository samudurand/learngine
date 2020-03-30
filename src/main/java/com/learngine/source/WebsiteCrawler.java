package com.learngine.source;

import org.apache.commons.lang3.NotImplementedException;

public abstract class WebsiteCrawler {

    protected Website website;

    public WebsiteCrawler(Website website) {
        this.website = website;
    }

    public void closeClient() {
        throw new NotImplementedException("Method not yet implemented");
    }
}
