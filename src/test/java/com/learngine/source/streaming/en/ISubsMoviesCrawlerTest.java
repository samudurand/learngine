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
class ISubsMoviesCrawlerTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().dynamicPort());

    ISubsMoviesCrawler crawler;
    String websiteUrl;

    @BeforeEach
    void setUp() {
        websiteUrl = "http://localhost:" + wireMockServer.port();
        ISubsMovies website = new ISubsMoviesCrawlerTest.MockWebsite(websiteUrl);
        crawler = new ISubsMoviesCrawler(website, new HeadlessCrawlerConfig().defaultWebClient());
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/isubsmovies/matrix.html");
        stubFor(get("/search/matrix").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("matrix");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/isubsmovies/no_results.html");
        stubFor(get("/search/no%20results").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "the matrix",
                        websiteUrl + "/movie/watch-the-matrix-online-0133093",
                        websiteUrl + "/admin/covers/27ed0fb950b856b06e1273989422e7d3",
                        "isubsmovies",
                        "I Subs Movies",
                        ""),
                new StreamCompleteDetails(
                        "the matrix reloaded",
                        websiteUrl + "/movie/watch-the-matrix-reloaded-online-0234215",
                        websiteUrl + "/admin/covers/5dd3e474f6e08e3316ce5e3bc36c666e",
                        "isubsmovies",
                        "I Subs Movies",
                        ""),
                new StreamCompleteDetails(
                        "the matrix revolutions",
                        websiteUrl + "/movie/watch-the-matrix-revolutions-online-0242653",
                        websiteUrl + "/admin/covers/acf666483bc8723fae7feda6f6a9cb7a",
                        "isubsmovies",
                        "I Subs Movies",
                        ""),
                new StreamCompleteDetails(
                        "the animatrix",
                        websiteUrl + "/movie/watch-the-animatrix-online-0328832",
                        websiteUrl + "/admin/covers/754abf02507264c05c99e9880a63bac2",
                        "isubsmovies",
                        "I Subs Movies",
                        "")
        );
    }

    static class MockWebsite extends ISubsMovies {
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