package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.streaming.StreamDetails;
import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class FiveMovies implements Website {
    @Override
    public String getName() {
        return "5 Movies";
    }

    @Override
    public String getUrl() {
        return "https://5movies.cloud";
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
            browser.get(String.format("%s/movie/search/%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.className("ml-item"))
                    .stream()
                    .map(elt -> {
                        return new StreamDetails(
                                elt.findElement(By.tagName("h2")).getText(),
                                elt.findElement(By.tagName("a")).getAttribute("href"),
                                website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
