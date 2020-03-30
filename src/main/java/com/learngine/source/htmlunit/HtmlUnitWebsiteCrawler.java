package com.learngine.source.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.config.SearchFailedException;
import com.learngine.source.Website;
import com.learngine.source.WebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class HtmlUnitWebsiteCrawler extends WebsiteCrawler {

    protected final WebClient client = defaultWebClient();

    public HtmlUnitWebsiteCrawler(Website website) {
        super(website);
        this.website = website;
    }

    public List<StreamDetails> searchStreamByTitle(String title) {
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
        client.close();
    }

    private WebClient defaultWebClient() {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setUseInsecureSSL(true);
        client.getCache().setMaxSize(0);
        client.setJavaScriptTimeout(10000);
        return client;
    }
}
