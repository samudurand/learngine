package com.learngine.source.streaming.fr;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilmFra implements Website {

    @Value("${streaming.urls.filmfra}")
    private String url;

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
        return url;
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
