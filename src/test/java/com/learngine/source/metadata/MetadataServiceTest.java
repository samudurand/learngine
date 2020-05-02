package com.learngine.source.metadata;

import com.learngine.api.MovieSearchResult;
import com.learngine.api.MovieSummary;
import com.learngine.common.Country;
import com.learngine.common.Language;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.MovieMetadata;
import com.learngine.source.metadata.domain.MovieMetadataSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetadataServiceTest {

    MovieMetadataSearchResult metadata;
    MovieSearchResult defaultResultMovies;
    @Mock
    private MetadataSource metadataSource;
    @InjectMocks
    private MetadataService service;

    @BeforeEach
    void setUp() {
        metadata = defaultTestMetadata();
        defaultResultMovies = defaultResultMovies();
    }

    @Test
    void retrieveMoviesMetadata() {
        when(metadataSource.searchMoviesByTitle("matrix", 1)).thenReturn(Mono.fromCallable(() -> metadata));

        var result = service.findMatchingMovies("matrix", 1).block();

        assertEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithoutDate() {
        metadata.getMovies().get(0).setReleaseDate(Optional.empty());
        when(metadataSource.searchMoviesByTitle("matrix", 1)).thenReturn(Mono.fromCallable(() -> metadata));

        var result = service.findMatchingMovies("matrix", 1).block();

        defaultResultMovies.getMovies().get(0).setDate(MetadataService.DEFAULT_RELEASE_DATE);
        assertEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithBadlyFormattedDate() {
        metadata.getMovies().get(0).setReleaseDate(Optional.of("2006-23-xx"));
        when(metadataSource.searchMoviesByTitle("matrix", 1)).thenReturn(Mono.fromCallable(() -> metadata));

        var result = service.findMatchingMovies("matrix", 1).block();

        defaultResultMovies.getMovies().get(0).setDate(MetadataService.DEFAULT_RELEASE_DATE);
        assertEquals(defaultResultMovies, result);
    }

    @Test
    void retrieveMoviesMetadataWithMissingDescription() {
        metadata.getMovies().get(0).setOverview(Optional.empty());
        when(metadataSource.searchMoviesByTitle("matrix", 1)).thenReturn(Mono.fromCallable(() -> metadata));

        var result = service.findMatchingMovies("matrix", 1).block();

        defaultResultMovies.getMovies().get(0).setDescription("");
        assertEquals(defaultResultMovies, result);
    }

    @Test
    void findAllTitlesInEnglish() {
        when(metadataSource.findAlternativeTitles(123)).thenReturn(Flux.fromIterable(matrixAlternativeTitles()));

        var result = service.findLocalizedTitles(123, Language.ENGLISH);

        assertIterableEquals(List.of("The Matrix", "The Matrix for USA"), result.toIterable());
    }

    @Test
    void findAllTitlesInEnglishWithoutDuplication() {
        var titles = matrixAlternativeTitles();
        titles.add(titles.get(0));
        when(metadataSource.findAlternativeTitles(123)).thenReturn(Flux.fromIterable(titles));

        var result = service.findLocalizedTitles(123, Language.ENGLISH);

        assertIterableEquals(List.of("The Matrix", "The Matrix for USA"), result.toIterable());
    }

    @Test
    void findNoTitlesInItalian() {
        when(metadataSource.findAlternativeTitles(123)).thenReturn(Flux.fromIterable(matrixAlternativeTitles()));

        var result = service.findLocalizedTitles(123, Language.ITALIAN);

        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void findNoTitlesForUnsupportedLanguage() {
        when(metadataSource.findAlternativeTitles(123)).thenReturn(Flux.fromIterable(matrixAlternativeTitles()));

        var result = service.findLocalizedTitles(123, Language.ITALIAN);

        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    private MovieMetadataSearchResult defaultTestMetadata() {
        return new MovieMetadataSearchResult(1, 2, 2,
                List.of(
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
        ));
    }

    private MovieSearchResult defaultResultMovies() {
        return new MovieSearchResult(
                2,
                List.of(
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
                ));
    }

    private List<AlternativeTitle> matrixAlternativeTitles() {
        var list = new ArrayList<AlternativeTitle>();
        list.add(new AlternativeTitle(Country.GB, "The Matrix"));
        list.add(new AlternativeTitle(Country.US, "The Matrix for USA"));
        list.add(new AlternativeTitle(Country.ES, "The Matrix por Espana"));
        return list;
    }
}