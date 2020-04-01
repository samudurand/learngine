package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.config.SearchFailedException;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import reactor.core.publisher.Flux;

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
    public List<StreamCompleteDetails> searchTitleByName(String title) {
        try {
            HtmlPage searchResultsPage = performSearch(title);
            List<StreamCompleteDetails> resultsFound = parseResults(searchResultsPage);

            log.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
            return resultsFound;
        } catch (IOException e) {
            log.error("Search failed on website " + website.getName(), e);
            throw new SearchFailedException();
        }
    }

    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) throws IOException {
        throw new NotImplementedException("Method not yet implemented");
    }

    /**
     * Navigate on the targeted website until it is in a state displaying the search results. Ready to be processed by {@link #parseResults(HtmlPage)}.
     */
    protected HtmlPage performSearch(String title) throws IOException {
        throw new NotImplementedException("Method not yet implemented");
    }

    /**
     * Assume the website is currently displaying the search results. See {@link #performSearch(String)}
     * Parse the web page content to extract all visible search results.
     */
    protected List<StreamCompleteDetails> parseResults(HtmlPage page) {
        throw new NotImplementedException("Method not yet implemented");
    }

    @Override
    public void closeClient() {
        getOrCreateClient().close();
    }
}
