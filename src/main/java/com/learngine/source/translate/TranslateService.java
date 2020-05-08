package com.learngine.source.translate;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TranslateService {

    private final GoogleCloudClient googleClient;

    public TranslateService(GoogleCloudClient googleClient) {
        this.googleClient = googleClient;
    }

    public Mono<String> translate(String text, String targetLanguage) {
        var normalizedTargetLanguage = normalizeLanguage(targetLanguage);
        return googleClient.translateFromEnglish(text, normalizedTargetLanguage);
    }

    private String normalizeLanguage(String targetLanguage) {
        return targetLanguage.split("-")[0];
    }
}
