package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;

/**
 * Selenium based web crawler. Slower and less reliable than the {@link HeadlessCrawler},
 * but necessary when the JS is too elaborate, or the website is protected, for instance by Cloudflare.
 */
@Slf4j
public abstract class UICrawler implements WebsiteCrawler {

    protected final Website website;
    private final Supplier<WebDriver> browserSupplier;
    private WebDriver browser;

    public UICrawler(Website website, Supplier<WebDriver> browserSupplier) {
        this.browserSupplier = browserSupplier;
        this.website = website;
    }

    @Override
    public Website getWebsite() {
        return website;
    }

    public WebDriver getOrCreateBrowser() {
        if (browser == null) {
            browser = browserSupplier.get();
        }
        return browser;
    }

    public void navigateToWebsite() {
        getOrCreateBrowser().get(website.getUrl());
    }

    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        throw new NotImplementedException("Method not yet implemented");
    }

    public List<StreamCompleteDetails> searchTitleByName(String title) {
        performSearch(title);
        List<StreamCompleteDetails> resultsFound = parseResults();

        log.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
        return resultsFound;
    }

    /**
     * Navigate on the targeted website until it is in a state displaying the search results. Ready to be processed by {@link #parseResults()}.
     */
    protected List<StreamCompleteDetails> parseResults() {
        throw new NotImplementedException("Method not yet implemented");
    }

    /**
     * Assume the website is currently displaying the search results. See {@link #performSearch(String)}
     * Parse the web page content to extract all visible search results.
     */
    protected void performSearch(String title) {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected Boolean isImageRetrievable() {
        return !website.isCloudflareProtected();
    }

    @Override
    public void closeClient() {
        getOrCreateBrowser().quit();
    }
}
