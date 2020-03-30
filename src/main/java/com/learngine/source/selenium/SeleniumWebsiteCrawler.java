package com.learngine.source.selenium;

import com.learngine.source.Website;
import com.learngine.source.WebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

@Configurable
@Slf4j
public abstract class SeleniumWebsiteCrawler extends WebsiteCrawler {

    protected final WebDriver browser;

    public SeleniumWebsiteCrawler(Website website, WebDriver browser) {
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

        log.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
        return resultsFound;
    }

    protected List<StreamDetails> parseResults() {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected void performSearch(String title) {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected Boolean isImageRetrievable() {
        return !website.isCloudflareProtected();
    }

    @Override
    public void closeClient() {
        browser.quit();
    }
}
