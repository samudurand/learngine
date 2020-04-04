package com.learngine.source.streaming.en;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.it.SeleniumTest;
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
class SolarMovieCrawlerTest implements SeleniumTest {

    @Managed
    final WireMockServer wireMockServer = with(wireMockConfig().port(SeleniumTest.EXPECTED_APP_PORT));

    SolarMovieCrawler crawler;
    String websiteUrl = SeleniumTest.EXPECTED_WEBSITE_URL;

    @BeforeEach
    void setUp() {
        SolarMovie website = new SolarMovieCrawlerTest.MockWebsite(websiteUrl);
        crawler = new SolarMovieCrawler(website, seleniumNode::getWebDriver);
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/matrix.html");
        stubFor(get("/movie/search/matrix.html").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("matrix");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/no_results.html");
        stubFor(get("/movie/search/no+results.html").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    @Test
    void performSearchForMatrixFailsWhenElementLacksTitleElement() throws IOException {
        String html = FileUtils.readFile("/html/solarmovie/missing_title.html");
        stubFor(get("/movie/search/notitle.html").willReturn(aResponse().withBody(html)));

        var resultFlux = crawler.performSearchAndParseResults("notitle");

        StepVerifier
                .create(resultFlux)
                .expectError(WebsiteCrawlingException.class)
                .verify();
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "the matrix (1999)",
                        "https://solarmoviefree.ac/movie/the-matrix-1999-1080p.44280.html",
                        "https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&" +
                                "gadget=a&no_expand=1&refresh=604800&url=" +
                                "https://img.moviescdn.live/crop/215/310/media/images/160713_124155/the-matrix-1999.jpg",
                        "solarmovie",
                        "Solar Movie",
                        "https://www1.solarmovie.to"),
                new StreamCompleteDetails(
                        "the matrix reloaded (2003)",
                        "https://solarmoviefree.ac/movie/the-matrix-reloaded-2003-1080p.73129.html",
                        "https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&gadget=a&" +
                                "no_expand=1&refresh=604800&url=" +
                                "https://img.moviescdn.live/crop/215/310/media/images/160713_015409/the-matrix-reloaded-2003.jpg",
                        "solarmovie",
                        "Solar Movie",
                        "https://www1.solarmovie.to"),
                new StreamCompleteDetails(
                        "the matrix revolutions (2003)",
                        "https://solarmoviefree.ac/movie/the-matrix-revolutions-2003-1080p.52700.html",
                        "https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?container=focus&gadget=a&" +
                                "no_expand=1&refresh=604800&url=" +
                                "https://img.moviescdn.live/crop/215/310/media/images/160905_040912/the-matrix-revolutions.jpg",
                        "solarmovie",
                        "Solar Movie",
                        "https://www1.solarmovie.to")
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