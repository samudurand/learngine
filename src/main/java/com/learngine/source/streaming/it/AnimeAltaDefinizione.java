package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.SeleniumBrowsable;
import com.learngine.source.SeleniumWebsiteHandler;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class AnimeAltaDefinizione implements Website, SeleniumBrowsable {

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
        return new Handler(this);
    }

    public static class Handler extends SeleniumWebsiteHandler {
        private final Logger logger = LoggerFactory.getLogger(Handler.class);

        public Handler(Website website) {
            super(website);
        }

        @Override
        protected void performSearch(String title) {
            browser.get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            var result = browser.findElements(By.className("article-image"));
            logger.info("Found {} elements", result.size());
            return browser.findElements(By.className("article-image"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.tagName("h3")).findElement(By.tagName("a"));
                        return new StreamDetails(link.getText(), link.getAttribute("href"), website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
