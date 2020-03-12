package com.learngine.source.streaming.fr;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class FilmFra implements Website {
    @Override
    public String getName() {
        return "Film Fra";
    }

    @Override
    public String getUrl() {
        return "http://filmfra.com";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.FRENCH;
    }

    @Override
    public Boolean isUiBasedSearch() {
        return true;
    }

    @Override
    public WebsiteHandler getHandler(WebDriver browser) {
        return new Handler(browser, this);
    }

    private static class Handler extends WebsiteHandler {

        public Handler(WebDriver browser, Website website) {
            super(browser, website);
        }

        /**
         * Submitting the search only works if the title is exactly formatted the same way on this website (casing included).
         * However the autocomplete feature on the search box is more flexible, for this reason we are only typing the searched name without submission.
         */
        @Override
        protected void performSearch(String title) {
            var searchTextField = browser.findElement(By.id("tags"));
            searchTextField.sendKeys(title);
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElement(By.id("ui-id-1")).findElements(By.tagName("li"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.tagName("a"));
                        return new StreamDetails(link.getText(), website.getUrl(), website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
