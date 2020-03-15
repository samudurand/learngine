package com.learngine.source;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebsiteHandler {

    protected Website website;
    private Logger logger = LoggerFactory.getLogger(WebsiteHandler.class);

    public WebsiteHandler(Website website) {
        this.website = website;
    }

    public void closeClient() {
        throw new NotImplementedException("Method not yet implemented");
    }
}
