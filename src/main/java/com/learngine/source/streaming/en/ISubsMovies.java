package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.alternativeEncodeSearchParams;

public class ISubsMovies implements Website {
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
    public WebsiteHandler getHandler(WebDriver browser) {
        return new Handler(browser, this);
    }

    public static class Handler extends WebsiteHandler {

        public Handler(WebDriver browser, Website website) {
            super(browser, website);
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
