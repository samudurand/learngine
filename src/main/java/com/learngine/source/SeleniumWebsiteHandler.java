package com.learngine.source;

import com.learngine.source.streaming.StreamDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class SeleniumWebsiteHandler extends WebsiteHandler {

    protected WebDriver browser = defaultBrowser();
    private Logger logger = LoggerFactory.getLogger(SeleniumWebsiteHandler.class);

    public SeleniumWebsiteHandler(Website website) {
        super(website);
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
    protected void closeClient() {
        browser.quit();
    }

    private WebDriver defaultBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        var browser = new ChromeDriver(options);
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return browser;
    }
}
