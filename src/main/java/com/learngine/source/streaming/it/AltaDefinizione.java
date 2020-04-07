package com.learngine.source.streaming.it;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AltaDefinizione implements Website {

    @Value("${streaming.urls.altadefinizione}")
    private String url;

    @Override
    public String getId() {
        return "altadefinizione";
    }

    @Override
    public String getName() {
        return "Alta Definizione";
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Optional<String> getReferenceUrl() {
        return Optional.of("https://altadefinizione-nuovo.info");
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
