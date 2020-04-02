package com.learngine.source.streaming.en;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
import com.learngine.crawler.HeadlessCrawlerConfig;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(WireMockExtension.class)
class SolarMovieCrawlerTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

    SolarMovieCrawler crawler;
    String websiteUrl;

    @BeforeEach
    void setUp() {
        websiteUrl = "http://localhost:" + wireMockServer.port();
        SolarMovie website = new SolarMovieCrawlerTest.MockWebsite(websiteUrl);
        crawler = new SolarMovieCrawler(website, new HeadlessCrawlerConfig().defaultWebClient());
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/matrix.html");
        stubFor(get("/search/matrix").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("matrix");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/no_results.html");
        stubFor(get("/search/no%20results").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    @Test
    void performSearchForMatrixFailsWhenElementLacksTitleElement() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/missing_title.html");
        stubFor(get("/search/notitle").willReturn(aResponse().withBody(html)));

        var resultFlux = crawler.performSearchAndParseResults("notitle");

        StepVerifier
                .create(resultFlux)
                .expectError(NullPointerException.class)
                .verify();
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "the matrix revolutions",
                        "https://solarmovie.network/movies/the-matrix-revolutions-27833",
                        "https://image.tmdb.org/t/p/w92/sKogjhfs5q3azmpW7DFKKAeLEG8.jpg",
                        "solarmovie",
                        "Solar Movie"),
                new StreamCompleteDetails(
                        "the matrix reloaded",
                        "https://solarmovie.network/movies/the-matrix-reloaded-27832",
                        "https://image.tmdb.org/t/p/w92/ezIurBz2fdUc68d98Fp9dRf5ihv.jpg",
                        "solarmovie",
                        "Solar Movie"),
                new StreamCompleteDetails(
                        "the matrix",
                        "https://solarmovie.network/movies/the-matrix-28276",
                        "https://image.tmdb.org/t/p/w92/hEpWvX6Bp79eLxY1kX5ZZJcme5U.jpg",
                        "solarmovie",
                        "Solar Movie")
        );
    }

    static class MockWebsite extends SolarMovie {
        private final String wiremockUrl;

        public MockWebsite(String wiremockUrl) {
            this.wiremockUrl = wiremockUrl;
        }

        @Override
        public String getUrl() {
            return wiremockUrl;
        }
    }
}