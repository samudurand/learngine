package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.stereotype.Component;

@Component
public class AnimeAltaDefinizione implements Website {
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

}
