package com.learngine.source.streaming.fr;

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
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ExtendWith(WireMockExtension.class)
class FilmFraCrawlerTest implements SeleniumTest {

    @Managed
    final WireMockServer wireMockServer = with(wireMockConfig().port(SeleniumTest.EXPECTED_APP_PORT));

    FilmFraCrawler crawler;
    String websiteUrl = SeleniumTest.EXPECTED_WEBSITE_URL;

    @BeforeEach
    void setUp() {
        FilmFra website = new FilmFraCrawlerTest.MockWebsite(websiteUrl);
        crawler = new FilmFraCrawler(website, seleniumNode::getWebDriver);

        stubFor(get(anyUrl()).willReturn(notFound()));
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/filmfra/contact.html");
        stubFor(get("/").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("contact");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/filmfra/no_results.html");
        stubFor(get("/").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    @Test
    void performSearchForMatrixFailsWhenElementLacksAnchorElement() throws IOException {
        String html = FileUtils.readFile("/html/filmfra/missing_name.html");
        stubFor(get("/").willReturn(aResponse().withBody(html)));

        var resultFlux = crawler.performSearchAndParseResults("notitle");

        StepVerifier
                .create(resultFlux)
                .expectError(WebsiteCrawlingException.class)
                .verify();
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "contact",
                        EXPECTED_WEBSITE_URL,
                        "",
                        "filmfra",
                        "Film Fra")
        );
    }

    static class MockWebsite extends FilmFra {
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