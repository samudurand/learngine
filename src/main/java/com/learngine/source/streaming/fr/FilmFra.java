package com.learngine.source.streaming.fr;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class FilmFra implements Website, SeleniumBrowsable {

    private final Supplier<WebDriver> browserSupplier;

    public FilmFra(Supplier<WebDriver> browserSupplier) {
        this.browserSupplier = browserSupplier;
    }

    @Override
    public String getId() {
        return "filmfra";
    }

    @Override
    public String getName() {
        return "Film Fra";
    }

    @Override
    public String getUrl() {
        return "http://filmfra.com";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.FRENCH;
    }

    @Override
    public Boolean isUiBasedSearch() {
        return true;
    }

    @Override
    public SeleniumWebsiteCrawler getHandler() {
        return new FilmFraCrawler(this, browserSupplier.get());
    }

}
