package com.learngine.crawler;

import com.learngine.source.Website;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

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

    public WebDriver getBrowser() {
        if (browser == null) {
            browser = browserSupplier.get();
        }
        return browser;
    }

    public void navigateToWebsite() {
        getBrowser().get(website.getUrl());
    }

    protected Boolean isImageRetrievable() {
        return !website.isCloudflareProtected();
    }

    @Override
    public void closeClient() {
        getBrowser().quit();
    }
}
