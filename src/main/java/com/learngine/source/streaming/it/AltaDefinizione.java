package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class AltaDefinizione implements Website {
    @Override
    public String getId() {
        return "altadefinizione";
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
}
