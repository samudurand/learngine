package com.learngine.source.streaming.engine;

import com.learngine.config.SearchFailedException;
import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class UnogCrawler extends UICrawler {

    public UnogCrawler(Unog website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected void performSearch(String title) {
        try {
            // Prevent failed loading of the main page
            getOrCreateBrowser().get("http://localhost");
            navigateToWebsite();

            // Wait a little to leave some time to the JS to execute
            Thread.sleep(2000);

            var searchTextField = getOrCreateBrowser().findElement(By.id("atitle"));
            var searchButton = getOrCreateBrowser().findElement(By.id("asfbutton"));
            searchTextField.sendKeys(title);
            searchButton.click();

        } catch (Exception e) {
            throw new SearchFailedException(e);
        }
    }

    @Override
    protected List<StreamCompleteDetails> parseResults() {
        return getOrCreateBrowser().findElements(By.className("videodiv"))
                .stream()
                .map(elt -> new StreamCompleteDetails(
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
        return String.format(Unog.netflixUrlPattern, netflixId);
    }
}
