package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnimeAltaDefinizione implements Website {

    @Value("${streaming.urls.animealtadefinizione}")
    private String url;

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
        return url;
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
