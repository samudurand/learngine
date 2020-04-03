package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.learngine.source.Website;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * HtmlUnit library based crawler. Much faster than the Selenium traditional crawling.
 */
@Slf4j
public abstract class HeadlessCrawler implements WebsiteCrawler {

    protected final Website website;
    private final Supplier<WebClient> clientSupplier;
    private WebClient client;

    public HeadlessCrawler(Website website, Supplier<WebClient> clientSupplier) {
        this.website = website;
        this.clientSupplier = clientSupplier;
    }

    @Override
    public Website getWebsite() {
        return website;
    }

    public WebClient getOrCreateClient() {
        if (client == null) {
            client = clientSupplier.get();
        }
        return client;
    }

    @Override
    public void closeClient() {
        getOrCreateClient().close();
    }
}
