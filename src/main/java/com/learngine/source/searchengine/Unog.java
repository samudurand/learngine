package com.learngine.source.searchengine;

import com.learngine.common.Language;
import com.learngine.config.SearchFailedException;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class Unog implements Website, SearchEngine, SeleniumBrowsable {

    private static final String netflixUrlPattern = "https://netflix.com/title/%s";
    private final Supplier<WebDriver> browserSupplier;

    public Unog(Supplier<WebDriver> browser) {
        this.browserSupplier = browser;
    }

    @Override
    public String getId() {
        return "netflix";
    }

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
    public SeleniumWebsiteCrawler getHandler() {
        return new Crawler(this, browserSupplier.get());
    }

    public static class Crawler extends SeleniumWebsiteCrawler {

        public Crawler(Website website, WebDriver browser) {
            super(website, browser);
        }

        @Override
        protected void performSearch(String title) {
            try {
                // Prevent failed loading of the main page
                browser.get("http://localhost");
                navigateToWebsite();

                // Attempt to handle ads redirection
                //if (!browser.getCurrentUrl().contains(website.getUrl())) {
                //    navigateToWebsite();
                //}

                // Wait a little to leave some time to the JS to execute
                Thread.sleep(2000);

                var searchTextField = browser.findElement(By.id("atitle"));
                var searchButton = browser.findElement(By.id("asfbutton"));
                searchTextField.sendKeys(title);
                searchButton.click();

            } catch (Exception e) {
                throw new SearchFailedException();
            }
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.className("videodiv"))
                    .stream()
                    .map(elt -> new StreamDetails(
                            elt.findElement(By.tagName("b")).getText(),
                            convertNetflixURL(elt.findElement(By.xpath(".//parent::a")).getAttribute("href")),
                            isImageRetrievable() ? elt.findElement(By.tagName("img")).getAttribute("src") : "",
                            website.getId(),
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
