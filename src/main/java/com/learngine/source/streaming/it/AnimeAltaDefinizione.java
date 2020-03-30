package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

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
    public SeleniumWebsiteCrawler getHandler() {
        return new AnimeAltaDefinizioneCrawler(this, browserSupplier.get());
    }

}
