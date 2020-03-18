package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizione implements Website, SeleniumBrowsable {

    private final Supplier<WebDriver> browserSupplier;

    public AltaDefinizione(Supplier<WebDriver> browserSupplier) {
        this.browserSupplier = browserSupplier;
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
    public Optional<String> getReferenceUrl() {
        return Optional.of("https://altadefinizione-nuovo.info");
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
