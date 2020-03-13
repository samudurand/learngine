package com.learngine.source.streaming.fr;

import com.learngine.common.Language;
import com.learngine.source.SeleniumBrowsable;
import com.learngine.source.SeleniumWebsiteHandler;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class StreamComplet implements Website, SeleniumBrowsable {
    @Override
    public String getName() {
        return "Stream Complet";
    }

    @Override
    public String getUrl() {
        return "https://www.streamcomplet.page";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.FRENCH;
    }

    @Override
    public SeleniumWebsiteHandler getHandler() {
        return new Handler(this);
    }

    private static class Handler extends SeleniumWebsiteHandler {

        public Handler(Website website) {
            super(website);
        }

        @Override
        protected void performSearch(String title) {
            browser.get(String.format("%s/fr/search/?s=%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.className("streamcompletpage_moviefilm"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.className("streamcompletpage_movief")).findElement(By.tagName("a"));
                        return new StreamDetails(link.getText(), link.getAttribute("href"), website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
