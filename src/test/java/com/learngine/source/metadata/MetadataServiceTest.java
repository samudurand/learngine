package com.learngine.source.metadata;

import com.learngine.api.domain.MovieSummary;
import com.learngine.source.metadata.domain.MovieMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

    @Mock
    private TheMovieDB metadataSource;

    @InjectMocks
    private MetadataService service;

    List<MovieMetadata> metadata;
    List<MovieSummary> defaultResultMovies;

    @BeforeEach
    void setUp() {
        metadata = defaultTestMetadata();
        defaultResultMovies = defaultResultMovies();
    }

    @Test
    void retrieveMoviesMetadata() {
        when(metadataSource.findMoviesByTitle("matrix")).thenReturn(Flux.fromIterable(metadata));

        var result = service.findMatchingMovies("matrix").toIterable();

        assertIterableEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithoutDate() {
        metadata.get(0).setReleaseDate(Optional.empty());
        when(metadataSource.findMoviesByTitle("matrix")).thenReturn(Flux.fromIterable(metadata));

        var result = service.findMatchingMovies("matrix").toIterable();

        defaultResultMovies.get(0).setDate(MetadataService.DEFAULT_RELEASE_DATE);
        assertIterableEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithBadlyFormattedDate() {
        metadata.get(0).setReleaseDate(Optional.of("2006-23-xx"));
        when(metadataSource.findMoviesByTitle("matrix")).thenReturn(Flux.fromIterable(metadata));

        var result = service.findMatchingMovies("matrix").toIterable();

        defaultResultMovies.get(0).setDate(MetadataService.DEFAULT_RELEASE_DATE);
        assertIterableEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithMissingDescription() {
        metadata.get(0).setOverview(Optional.empty());
        when(metadataSource.findMoviesByTitle("matrix")).thenReturn(Flux.fromIterable(metadata));

        var result = service.findMatchingMovies("matrix").toIterable();

        defaultResultMovies.get(0).setDescription("");
        assertIterableEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataOrderedByVoteAverage() {
        metadata.get(0).setVoteAverage(2f);
        when(metadataSource.findMoviesByTitle("matrix")).thenReturn(Flux.fromIterable(metadata));

        var result = service.findMatchingMovies("matrix").toIterable();

        defaultResultMovies.get(0).setVoteAverage(2f);
        var expectedResult = List.of(defaultResultMovies.get(1), defaultResultMovies.get(0));
        assertIterableEquals(expectedResult, result);
    }

    private List<MovieMetadata> defaultTestMetadata() {
        return List.of(
                new MovieMetadata(1,
                        "The Matrix",
                        Optional.of("1999-03-13"),
                        Optional.of("/matrix.png"),
                        Optional.of("a great movie"),
                        7.5f),
                new MovieMetadata(20,
                        "The Matrix revolution",
                        Optional.of("2015-05-27"),
                        Optional.of("/mtx-revolution.jpg"),
                        Optional.of("another Great movie"),
                        7f)
        );
    }

    private List<MovieSummary> defaultResultMovies() {
        return List.of(
                new MovieSummary(1,
                        "the matrix",
                        "http://image.tmdb.org/t/p/w92/matrix.png",
                        LocalDate.parse("1999-03-13"),
                        "a great movie",
                        7.5f),
                new MovieSummary(20,
                        "the matrix revolution",
                        "http://image.tmdb.org/t/p/w92/mtx-revolution.jpg",
                        LocalDate.parse("2015-05-27"),
                        "another Great movie",
                        7f)
        );
    }
}