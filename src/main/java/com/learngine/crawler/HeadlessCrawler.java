package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.config.SearchFailedException;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.util.List;
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
    public List<StreamDetails> searchTitleByName(String title) {
        try {
            HtmlPage searchResultsPage = performSearch(title);
            List<StreamDetails> resultsFound = parseResults(searchResultsPage);

            log.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
            return resultsFound;
        } catch (IOException e) {
            log.error("Search failed on website " + website.getName(), e);
            throw new SearchFailedException();
        }
    }

    protected HtmlPage performSearch(String title) throws IOException {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected List<StreamDetails> parseResults(HtmlPage page) {
        throw new NotImplementedException("Method not yet implemented");
    }

    @Override
    public void closeClient() {
        getOrCreateClient().close();
    }
}
