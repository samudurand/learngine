package com.learngine.source.translate;

import reactor.core.publisher.Mono;

public interface GoogleCloudClient {
    Mono<String> translateFromEnglish(String text, String targetLanguage);
}
