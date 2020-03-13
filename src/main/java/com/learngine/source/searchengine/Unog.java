package com.learngine.source.searchengine;

import com.google.common.collect.Iterables;
import com.learngine.common.Language;
import com.learngine.configuration.SearchedFailedException;
import com.learngine.source.SeleniumBrowsable;
import com.learngine.source.SeleniumWebsiteHandler;
import com.learngine.source.WebCrawler;
import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Unog implements Website, SearchEngine, SeleniumBrowsable {

    private static final String netflixUrlPattern = "https://netflix.com/title/%s";

    @Override
    public String getName() {
        return "Netflix";
    }

    @Override
    public String getUrl() {
        return "https://unogs.com";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }

    @Override
    public Boolean isUiBasedSearch() {
        return true;
    }

    @Override
    public SeleniumWebsiteHandler getHandler() {
        return new Handler(this);
    }

    public static class Handler extends SeleniumWebsiteHandler {
        private final Logger logger = LoggerFactory.getLogger(Handler.class);

        public Handler(Website website) {
            super(website);
        }

        @Override
        protected void performSearch(String title) {
            var searchTextField = browser.findElement(By.id("atitle"));
            var searchButton = browser.findElement(By.id("asfbutton"));

            try {
                // Wait a little to leave some time to the JS to execute
                Thread.sleep(2000);
                searchTextField.sendKeys(title);
                searchButton.click();
            } catch (InterruptedException e) {
                throw new SearchedFailedException();
            }
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.className("videodiv"))
                    .stream()
                    .map(elt -> new StreamDetails(
                            elt.findElement(By.tagName("b")).getText(),
                            convertNetflixURL(elt.findElement(By.xpath(".//parent::a")).getAttribute("href")),
                            website.getName()
                    ))
                    .collect(Collectors.toList());
        }

        private String convertNetflixURL(String unogLink) {
            final var urlParts = unogLink.split("=");
            final var netflixId = urlParts[urlParts.length - 1];
            return String.format(netflixUrlPattern, netflixId);
        }
    }
}
