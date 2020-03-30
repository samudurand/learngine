package com.learngine.source.searchengine;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import com.learngine.source.streaming.SearchEngine;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class Unog implements Website, SearchEngine {

    public static final String netflixUrlPattern = "https://netflix.com/title/%s";

    @Override
    public String getId() {
        return "netflix";
    }

    @Override
    public String getName() {
        return "Netflix";
    }

    @Override
    public String getUrl() {
        return "https://unogs.com";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }

    @Override
    public Boolean isUiBasedSearch() {
        return true;
    }
}
