package com.learngine.source.selenium;

import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

@Configurable
public abstract class SeleniumWebsiteHandler extends WebsiteHandler {

    protected final WebDriver browser;

    private final Logger logger = LoggerFactory.getLogger(SeleniumWebsiteHandler.class);

    public SeleniumWebsiteHandler(Website website, WebDriver browser) {
        super(website);
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

    @Override
    public void closeClient() {
        browser.quit();
    }
}
