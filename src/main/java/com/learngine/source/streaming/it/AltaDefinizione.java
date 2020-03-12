package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.WebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class AltaDefinizione implements Website {

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
    public WebsiteHandler getHandler(WebDriver browser) {
        return new Handler(browser, this);
    }

    public static class Handler extends WebsiteHandler {
        public Handler(WebDriver browser, Website website) {
            super(browser, website);
        }

        @Override
        protected void performSearch(String title) {
            browser.get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults() {
            return browser.findElements(By.xpath("//h2[@class='titleFilmMobile']"))
                    .stream()
                    .map(elt -> {
                        var link = elt.findElement(By.tagName("a"));
                        return new StreamDetails(link.getText(), link.getAttribute("href"), website.getName());
                    })
                    .collect(Collectors.toList());
        }
    }
}
