package com.learngine.source.metadata;

import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.MovieMetadataSearchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MetadataSource {
    Mono<MovieMetadataSearchResult> searchMoviesByTitle(String title, Integer page);

    Flux<AlternativeTitle> findAlternativeTitles(int movieId);
}
