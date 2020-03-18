package com.learngine.source.metadata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.learngine.configuration.MetadataRetrievalFailedException;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.AlternativeTitleSearchResult;
import com.learngine.source.metadata.domain.MovieMetadata;
import com.learngine.source.metadata.domain.MovieMetadataSearchResult;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class TheMovieDB {
    
    final CloseableHttpClient client = HttpClients.createDefault();
    private final Logger logger = LoggerFactory.getLogger(TheMovieDB.class);
    @Value("${themoviedb.url}")
    private String baseUrl;

    @Value("${themoviedb.apiVersion}")
    private String apiVersion;

    @Value("${themoviedb.apiToken}")
    private String apiToken;

    private ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .registerModule(new Jdk8Module());

    public Flux<MovieMetadata> findMoviesByTitle(String title) {
        var searchUrl = String.format("%s/%s/search/movie?api_key=%s&query=%s", baseUrl, apiVersion, apiToken, encodeSearchParams(title));
        logger.info(searchUrl);
        try {
            var response = client.execute(new HttpGet(searchUrl));
            final var movies = mapper.readValue(response.getEntity().getContent(), MovieMetadataSearchResult.class).getMovies();
            logger.debug("Movies found for title '{}': \n {}", title, movies);
            return Flux.fromIterable(movies);
        } catch (IOException e) {
            logger.error("Could not retrieve titles matching '" + title + "'", e);
            throw new MetadataRetrievalFailedException();
        }
    }

    public Flux<AlternativeTitle> findAlternativeTitles(int movieId) {
        var searchUrl = String.format("%s/%s/movie/%d/alternative_titles?api_key=%s", baseUrl, apiVersion, movieId, apiToken);
        try {
            var response = client.execute(new HttpGet(searchUrl));
            final var titles = mapper.readValue(response.getEntity().getContent(), AlternativeTitleSearchResult.class).getTitles();
            logger.debug("Alternative titles for movie '{}': \n {}", movieId, titles);
            return Flux.fromIterable(titles);
        } catch (IOException e) {
            logger.error("Could not retrieve alternative titles for movie '" + movieId + "'", e);
            throw new MetadataRetrievalFailedException();
        }
    }
}
