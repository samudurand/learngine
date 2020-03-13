package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.SeleniumBrowsable;
import com.learngine.source.Website;
import com.learngine.source.SeleniumWebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.alternativeEncodeSearchParams;

public class ISubsMovies implements Website, SeleniumBrowsable {
    @Override
    public String getName() {
        return "I Subs Movies";
    }

    @Override
    public String getUrl() {
        return "https://isubsmovies.com";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }

    @Override
    public SeleniumWebsiteHandler getHandler() {
        return new Handler(this);
    }

    public static class Handler extends SeleniumWebsiteHandler {

        public Handler(Website website) {
            super(website);
        }

        @Override
        protected void performSearch(String title) {
            browser.get(String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.tagName("figcaption"))
                    .stream()
                    .map(elt -> new StreamDetails(
                            elt.findElement(By.tagName("h2")).getText(),
                            elt.findElement(By.xpath(".//parent::figure/parent::a")).getAttribute("href"),
                            website.getName())
                    )
                    .collect(Collectors.toList());
        }
    }
}
