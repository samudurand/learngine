package com.learngine.source.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(value = "translate.google.enabled", havingValue = "false", matchIfMissing = true)
public class GoogleCloudClientMock implements GoogleCloudClient {

    public Mono<String> translateFromEnglish(String text, String targetLanguage) {
        return Mono.just(text);
    }
}