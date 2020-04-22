package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SolarMovie implements Website {

    @Value("${streaming.solarmovie.url}")
    private String url;

    @Override
    public String getId() {
        return "solarmovie";
    }

    @Override
    public String getName() {
        return "Solar Movie";
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Optional<String> getAlternativeUrl() {
        return Optional.of("https://www1.solarmovie.to");
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }
}
