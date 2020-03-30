package com.learngine.source.selenium;

import com.learngine.source.Website;
import com.learngine.source.WebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public abstract class SeleniumWebsiteCrawler implements WebsiteCrawler {

    protected final Website website;
    private final Supplier<WebDriver> browserSupplier;
    private WebDriver browser;

    public SeleniumWebsiteCrawler(Website website, Supplier<WebDriver> browserSupplier) {
        this.browserSupplier = browserSupplier;
        this.website = website;
    }

    @Override
    public Website getWebsite() {
        return website;
    }

    public WebDriver getBrowser() {
        if (browser != null) {
            browser = browserSupplier.get();
        }
        return browser;
    }

    public void navigateToWebsite() {
        getBrowser().get(website.getUrl());
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
        getBrowser().quit();
    }
}
