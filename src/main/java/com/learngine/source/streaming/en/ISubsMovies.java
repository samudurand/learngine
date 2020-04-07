package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ISubsMovies implements Website {

    @Value("${streaming.urls.isubsmovies}")
    private String url;

    @Override
    public String getId() {
        return "isubsmovies";
    }

    @Override
    public String getName() {
        return "I Subs Movies";
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }
}
