package com.learngine.source.metadata;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.common.Country;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.MovieMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(WireMockExtension.class)
class TheMovieDBTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

    TheMovieDB theMovieDB;

    @BeforeEach
    void setUp() {
        var serverUrl = "http://localhost:" + wireMockServer.port();
        theMovieDB = buildAndSetupTestMovieDB(serverUrl);
    }

    @Test
    void searchMoviesWithTitleMatrix() {
        var result = theMovieDB.searchMoviesByTitle("matrix");
        assertIterableEquals(matrixMovies(), result.toIterable());
    }

    @Test
    void searchMoviesWithNonExistingTitleMatrixReturnsNoResults() {
        var result = theMovieDB.searchMoviesByTitle("neverproduced");
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void searchMoviesReturnsEmptyWhenJSONIsMissingData() {
        var result = theMovieDB.searchMoviesByTitle("missingattribute");
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void searchMoviesReturnsEmptyWhenJSONIsBadlyFormatted() {
        var result = theMovieDB.searchMoviesByTitle("badjson");
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void findAlternativeTitlesWithMatrixMovieId() {
        var result = theMovieDB.findAlternativeTitles(603);
        assertIterableEquals(matrixTitles(), result.toIterable());
    }

    @Test
    void findAlternativeTitlesWithNoAlternativesReturnsNoResults() {
        var result = theMovieDB.findAlternativeTitles(10);
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void findAlternativeTitlesReturnsEmptyWhenJSONIsMissingData() {
        var result = theMovieDB.findAlternativeTitles(0);
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    @Test
    void findAlternativeTitlesReturnsEmptyWhenJSONIsBadlyFormatted() {
        var result = theMovieDB.findAlternativeTitles(1);
        assertIterableEquals(Collections.emptyList(), result.toIterable());
    }

    List<AlternativeTitle> matrixTitles() {
        return List.of(
                new AlternativeTitle(Country.ES, "Matrix 1999"),
                new AlternativeTitle(Country.IT, "Matrix I"),
                new AlternativeTitle(Country.US, "The Matrix 1")
        );
    }

    List<MovieMetadata> matrixMovies() {
        return List.of(
                new MovieMetadata(603, "The Matrix", Optional.of("1999-03-30"), Optional.of("/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg"), Optional.of("Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth."), 8.1f),
                new MovieMetadata(604, "The Matrix Reloaded", Optional.of("2003-05-15"), Optional.of("/ezIurBz2fdUc68d98Fp9dRf5ihv.jpg"), Optional.of("Six months after the events depicted in The Matrix, Neo has proved to be a good omen for the free humans, as more and more humans are being freed from the matrix and brought to Zion, the one and only stronghold of the Resistance.  Neo himself has discovered his superpowers including super speed, ability to see the codes of the things inside the matrix and a certain degree of pre-cognition. But a nasty piece of news hits the human resistance: 250,000 machine sentinels are digging to Zion and would reach them in 72 hours. As Zion prepares for the ultimate war, Neo, Morpheus and Trinity are advised by the Oracle to find the Keymaker who would help them reach the Source.  Meanwhile Neo's recurrent dreams depicting Trinity's death have got him worried and as if it was not enough, Agent Smith has somehow escaped deletion, has become more powerful than before and has fixed Neo as his next target."), 6.9f),
                new MovieMetadata(605, "The Matrix Revolutions", Optional.of("2003-11-03"), Optional.of("/sKogjhfs5q3azmpW7DFKKAeLEG8.jpg"), Optional.of("The human city of Zion defends itself against the massive invasion of the machines as Neo fights to end the war at another front while also opposing the rogue Agent Smith."), 6.6f)
        );
    }

    private TheMovieDB buildAndSetupTestMovieDB(String serverUrl) {
        var theMovieDB = new TheMovieDB();
        ReflectionTestUtils.setField(theMovieDB, "baseUrl", serverUrl);
        ReflectionTestUtils.setField(theMovieDB, "apiVersion", "3");
        ReflectionTestUtils.setField(theMovieDB, "apiToken", "token");
        return theMovieDB;
    }
//
//    @Test
//    void findAlternativeTitles() {
//    }
}