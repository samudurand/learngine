package com.learngine.source.metadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.learngine.config.MetadataRetrievalFailedException;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.AlternativeTitleSearchResult;
import com.learngine.source.metadata.domain.MovieMetadata;
import com.learngine.source.metadata.domain.MovieMetadataSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
@Slf4j
public class TheMovieDB {

    final WebClient webClient = WebClient.create();
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .registerModule(new Jdk8Module());
    @Value("${themoviedb.url}")
    private String baseUrl;
    @Value("${themoviedb.apiVersion}")
    private String apiVersion;
    @Value("${themoviedb.apiToken}")
    private String apiToken;

    public Flux<MovieMetadata> findMoviesByTitle(String title) {
        var searchUrl = String.format("%s/%s/search/movie?api_key=%s&query=%s", baseUrl, apiVersion, apiToken, encodeSearchParams(title));
        return webClient.get().uri(searchUrl).exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .flatMapMany(body -> {
                    try {
                        return Flux.fromIterable(mapper.readValue(body, MovieMetadataSearchResult.class).getMovies());
                    } catch (IOException e) {
                        log.error("Could not retrieve titles matching '" + title + "'", e);
                        throw new MetadataRetrievalFailedException();
                    }
                });
    }

    public Flux<AlternativeTitle> findAlternativeTitles(int movieId) {
        var searchUrl = String.format("%s/%s/movie/%d/alternative_titles?api_key=%s", baseUrl, apiVersion, movieId, apiToken);
        return webClient.get().uri(searchUrl).exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .flatMapMany(body -> {
                    try {
                        return Flux.fromIterable(mapper.readValue(body, AlternativeTitleSearchResult.class).getTitles());
                    } catch (Exception e) {
                        log.error("Could not retrieve alternative titles for movie '" + movieId + "'", e);
                        return Flux.empty();
                    }
                });
    }
}
