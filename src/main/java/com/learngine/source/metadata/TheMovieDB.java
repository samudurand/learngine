package com.learngine.source.metadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.AlternativeTitleSearchResult;
import com.learngine.source.metadata.domain.MovieMetadata;
import com.learngine.source.metadata.domain.MovieMetadataSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static com.learngine.source.utils.HttpUtils.encodeRequestParams;

@Component
@Slf4j
public class TheMovieDB implements MetadataSource {

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper mapper = buildObjectMapper();

    @Value("${themoviedb.url}")
    private String baseUrl;
    @Value("${themoviedb.apiVersion}")
    private String apiVersion;
    @Value("${themoviedb.apiToken}")
    private String apiToken;

    @Override
    public Flux<MovieMetadata> searchMoviesByTitle(String title, Integer page) {
        var searchUrl = buildSearchMoviesUrl(title, page);
        var authHeader = buildAuhtorizationHeader();
        return webClient.get().uri(searchUrl).header("Authorization", authHeader).exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .flatMapMany(parseBodyIntoMoviesMetadata(title));
    }

    private String buildAuhtorizationHeader() {
        return String.format("Bearer %s", apiToken);
    }

    private String buildSearchMoviesUrl(String title, Integer page) {
        return String.format("%s/%s/search/movie?query=%s&page=%d",
                baseUrl, apiVersion, encodeRequestParams(title), page);
    }

    private Function<String, Publisher<? extends MovieMetadata>> parseBodyIntoMoviesMetadata(String title) {
        return body -> {
            try {
                var movieMetadataList = mapper.readValue(body, MovieMetadataSearchResult.class).getMovies();
                return Flux.fromIterable(movieMetadataList);
            } catch (Exception e) {
                log.error("Could not retrieve titles matching '" + title + "'", e);
                return Flux.empty();
            }
        };
    }

    @Override
    public Flux<AlternativeTitle> findAlternativeTitles(int movieId) {
        var searchUrl = buildSearchAlternativeTitlesUrl(movieId);
        return webClient.get().uri(searchUrl).exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .flatMapMany(parseBodyIntoTitlesList(movieId));
    }

    private String buildSearchAlternativeTitlesUrl(int movieId) {
        return String.format("%s/%s/movie/%d/alternative_titles", baseUrl, apiVersion, movieId);
    }

    private Function<String, Publisher<? extends AlternativeTitle>> parseBodyIntoTitlesList(int movieId) {
        return body -> {
            try {
                return Flux.fromIterable(mapper.readValue(body, AlternativeTitleSearchResult.class).getTitles());
            } catch (Exception e) {
                log.error("Could not retrieve alternative titles for movie '" + movieId + "'", e);
                return Flux.empty();
            }
        };
    }

    private ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .registerModule(new Jdk8Module());
    }
}
