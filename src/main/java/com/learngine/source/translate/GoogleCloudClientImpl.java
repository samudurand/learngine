package com.learngine.source.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(value = "translate.google.enabled", havingValue = "true")
public class GoogleCloudClientImpl implements GoogleCloudClient {

    private static final String ENGLISH_LANG_CODE = "en";

    private final Translate translateService;

    public GoogleCloudClientImpl(Translate translateService) {
        this.translateService = translateService;
    }

    @Override
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