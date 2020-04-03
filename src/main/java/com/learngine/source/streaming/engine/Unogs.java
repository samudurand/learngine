package com.learngine.source.streaming.engine;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.streaming.SearchEngine;
import org.springframework.stereotype.Component;

@Component
public class Unogs implements Website, SearchEngine {

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
