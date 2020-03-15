package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteHandler;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizione implements Website, SeleniumBrowsable {

    private final WebDriver browser;

    public AltaDefinizione(WebDriver browser) {
        this.browser = browser;
    }

    @Override
    public String getName() {
        return "Alta Definizione";
    }

    @Override
    public String getUrl() {
        return "https://altadefinizione.style";
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
        return new Handler(this, browser);
    }

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
            return browser.findElements(By.xpath("//h5[@class='titleFilm']"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.xpath(".//parent::div/parent::div/parent::a"));
                        return new StreamDetails(elt.getText(), link.getAttribute("href"), website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
