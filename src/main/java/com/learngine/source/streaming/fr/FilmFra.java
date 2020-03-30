package com.learngine.source.streaming.fr;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class FilmFra implements Website {
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
}
