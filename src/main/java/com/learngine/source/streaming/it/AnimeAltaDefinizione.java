package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class AnimeAltaDefinizione implements Website, SeleniumBrowsable {

    private final Supplier<WebDriver> browserSupplier;

    public AnimeAltaDefinizione(Supplier<WebDriver> browserSupplier) {
        this.browserSupplier = browserSupplier;
    }

    @Override
    public String getId() {
        return "animealtadefinizione";
    }

    @Override
    public String getName() {
        return "Anime Alta Definizione";
    }

    @Override
    public String getUrl() {
        return "https://www.animealtadefinizione.it";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ITALIAN;
    }

    @Override
    public Boolean isCloudflareProtected() {
        return true;
    }

    @Override
    public SeleniumWebsiteHandler getHandler() {
        return new Handler(this, browserSupplier.get());
    }

    @Slf4j
    public static class Handler extends SeleniumWebsiteHandler {

        public Handler(Website website, WebDriver browser) {
            super(website, browser);
        }

        @Override
        protected void performSearch(String title) {
            browser.get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            var result = browser.findElements(By.className("article-image"));
            log.info("Found {} elements", result.size());
            return browser.findElements(By.className("article-image"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.tagName("h3")).findElement(By.tagName("a"));
                        var img = elt.findElement(By.tagName("a")).findElement(By.tagName("img"));
                        return new StreamDetails(
                                link.getText(),
                                link.getAttribute("href"),
                                isImageRetrievable() ? img.getAttribute("src") : "",
                                website.getId(),
                                website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
