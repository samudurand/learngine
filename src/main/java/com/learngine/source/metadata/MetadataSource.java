package com.learngine.source.metadata;

import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.MovieMetadata;
import reactor.core.publisher.Flux;

public interface MetadataSource {
    Flux<MovieMetadata> searchMoviesByTitle(String title, Integer page);

    Flux<AlternativeTitle> findAlternativeTitles(int movieId);
}
