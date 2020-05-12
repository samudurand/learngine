package com.learngine.source.translate;

import com.learngine.source.metadata.MetadataService;
import com.learngine.source.utils.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TranslateService {

    private static final Integer MAX_TEXT_LENGTH = 200;

    private final GoogleCloudClient googleClient;
    private final MetadataService metadataService;

    public TranslateService(final GoogleCloudClient googleClient, final MetadataService metadataService) {
        this.googleClient = googleClient;
        this.metadataService = metadataService;
    }

    public Mono<String> translateDescription(Integer movieId, String targetLanguage) {
        var normalizedTargetLanguage = normalizeLanguage(targetLanguage);
        return metadataService
                .getMovieDetails(movieId)
                .flatMap((movie) -> {
                    var limitedLengthDescription = StringUtils.truncate(movie.getDescription(), MAX_TEXT_LENGTH);
                    return googleClient.translateFromEnglish(
                            limitedLengthDescription,
                            normalizedTargetLanguage);
                });
    }

    private String normalizeLanguage(String targetLanguage) {
        return targetLanguage.split("-")[0];
    }
}
