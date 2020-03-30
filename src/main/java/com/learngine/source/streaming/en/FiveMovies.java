package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.stereotype.Component;

@Component
public class FiveMovies implements Website {
    @Override
    public String getId() {
        return "5movies";
    }

    @Override
    public String getName() {
        return "5 Movies";
    }

    @Override
    public String getUrl() {
        return "https://5movies.cloud";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }
}
