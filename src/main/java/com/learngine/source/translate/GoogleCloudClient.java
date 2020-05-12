package com.learngine.source.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GoogleCloudClient {

    private static final String ENGLISH_LANG_CODE = "en";

    private final Translate translateService;

    public GoogleCloudClient(Translate translateService) {
        this.translateService = translateService;
    }

    public Mono<String> translateFromEnglish(String text, String targetLanguage) {
        return Mono.fromCallable(() ->
                translateService.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage(ENGLISH_LANG_CODE),
                        Translate.TranslateOption.targetLanguage(targetLanguage),
                        Translate.TranslateOption.format("text")))
                .map(Translation::getTranslatedText);
    }

}