package com.learngine.source.streaming.en;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
import com.learngine.crawler.HeadlessCrawlerConfig;
import com.learngine.exception.WebsiteCrawlingException;
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

@ExtendWith(WireMockExtension.class)
class FiveMoviesCrawlerTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

    FiveMoviesCrawler crawler;

    @BeforeEach
    void setUp() {
        var websiteUrl = "http://localhost:" + wireMockServer.port();
        FiveMovies website = new MockWebsite(websiteUrl);
        crawler = new FiveMoviesCrawler(website, new HeadlessCrawlerConfig().defaultWebClient());
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/5movies/matrix.html");
        stubFor(get("/movie/search/matrix").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("matrix");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/5movies/no_results.html");
        stubFor(get("/movie/search/no+results").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    @Test
    void performSearchForMatrixFailsWhenElementLacksTitleElement() throws IOException {
        String html = FileUtils.readFile("/html/5movies/missing_title.html");
        stubFor(get("/movie/search/notitle").willReturn(aResponse().withBody(html)));

        var resultFlux = crawler.performSearchAndParseResults("notitle");

        StepVerifier
                .create(resultFlux)
                .expectError(WebsiteCrawlingException.class)
                .verify();
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "the matrix",
                        "https://5movies.cloud/film/the-matrix-1967/",
                        "https://i.vodn.in/p-max/200/the-matrix-1967.jpg",
                        "5movies",
                        "5 Movies",
                        ""),
                new StreamCompleteDetails(
                        "the matrix revolutions",
                        "https://5movies.cloud/film/the-matrix-revolutions-1969/",
                        "https://i.vodn.in/p-max/200/the-matrix-revolutions-1969.jpg",
                        "5movies",
                        "5 Movies",
                        ""),
                new StreamCompleteDetails(
                        "the matrix reloaded",
                        "https://5movies.cloud/film/the-matrix-reloaded-1968/",
                        "https://i.vodn.in/p-max/200/the-matrix-reloaded-1968.jpg",
                        "5movies",
                        "5 Movies",
                        "")
        );
    }

    static class MockWebsite extends FiveMovies {
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