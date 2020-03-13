package com.learngine.source;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.configuration.SearchedFailedException;
import com.learngine.source.streaming.StreamDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public abstract class HtmlUnitWebsiteHandler extends WebsiteHandler {

    protected WebClient client = defaultWebClient();
    private Logger logger = LoggerFactory.getLogger(HtmlUnitWebsiteHandler.class);

    public HtmlUnitWebsiteHandler(Website website) {
        super(website);
        this.website = website;
    }

    public HtmlPage navigateToWebsite() throws IOException {
        return client.getPage(website.getUrl());
    }

    public List<StreamDetails> searchTitleByName(String title) {
        try {
            HtmlPage searchResultsPage = performSearch(title);
            List<StreamDetails> resultsFound = parseResults(searchResultsPage);

            logger.debug("Found {} results for '{}' search: {}", resultsFound.size(), title, resultsFound);
            return resultsFound;
        } catch (IOException e) {
            logger.error("Could not perform search.", e);
            throw new SearchedFailedException();
        }
    }

    protected HtmlPage performSearch(String title) throws IOException {
        throw new NotImplementedException("Method not yet implemented");
    }

    protected List<StreamDetails> parseResults(HtmlPage page) {
        throw new NotImplementedException("Method not yet implemented");
    }

    @Override
    protected void closeClient() {
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

    protected String buildFullLink(String relativeLink) {
        return website.getUrl() + relativeLink;
    }
}
