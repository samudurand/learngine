package com.learngine.source;

import com.learngine.source.streaming.StreamDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class WebsiteHandler {

    protected WebDriver browser;
    protected Website website;
    private Logger logger = LoggerFactory.getLogger(WebsiteHandler.class);

    public WebsiteHandler(WebDriver browser, Website website) {
        this.browser = browser;
        this.website = website;
    }

    public void navigateToWebsite() {
        browser.get(website.getUrl());
    }

    public List<StreamDetails> searchTitleByName(String title) {
        performSearch(title);
        List<StreamDetails> resultsFound = parseResults();

        logger.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
        return resultsFound;
    }

    protected List<StreamDetails> parseResults() {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected void performSearch(String title) {
        throw new NotImplementedException("Method not yet implemented");
    }
}
