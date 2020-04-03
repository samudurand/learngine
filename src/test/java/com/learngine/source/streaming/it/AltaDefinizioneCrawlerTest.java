package com.learngine.source.streaming.it;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
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
class AltaDefinizioneCrawlerTest implements SeleniumTest {

    @Managed
    final WireMockServer wireMockServer = with(wireMockConfig().port(SeleniumTest.EXPECTED_APP_PORT));

    AltaDefinizioneCrawler crawler;
    String websiteUrl = SeleniumTest.EXPECTED_WEBSITE_URL;

    @BeforeEach
    void setUp() {
        AltaDefinizione website = new AltaDefinizioneCrawlerTest.MockWebsite(websiteUrl);
        crawler = new AltaDefinizioneCrawler(website, seleniumNode::getWebDriver);
    }

    @Test
    void performSearchForTitleMatrixAndParseResults() throws IOException {
        String html = FileUtils.readFile("/html/altaDefinizione/matrix.html");
        stubFor(get("/?s=matrix").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("matrix");

        Assertions.assertIterableEquals(matrixStreams(), results.toIterable());
    }

    @Test
    void performSearchForTitleWithoutResults() throws IOException {
        String html = FileUtils.readFile("/html/altaDefinizione/no_results.html");
        stubFor(get("/?s=no+results").willReturn(aResponse().withBody(html)));

        var results = crawler.performSearchAndParseResults("no results");

        Assertions.assertIterableEquals(List.of(), results.toIterable());
    }

    @Test
    void performSearchForMatrixFailsWhenElementLacksAnchorElement() throws IOException {
        String html = FileUtils.readFile("/html/altadefinizione/missing_link.html");
        stubFor(get("/?s=notitle").willReturn(aResponse().withBody(html)));

        var resultFlux = crawler.performSearchAndParseResults("notitle");

        StepVerifier
                .create(resultFlux)
                .expectError(WebsiteCrawlingException.class)
                .verify();
    }

    private List<StreamCompleteDetails> matrixStreams() {
        return List.of(
                new StreamCompleteDetails(
                        "matrix reloaded",
                        "https://altadefinizione.style/matrix-reloaded-streaming-ita/",
                        "",
                        "altadefinizione",
                        "Alta Definizione"),
                new StreamCompleteDetails(
                        "matrix",
                        "https://altadefinizione.style/matrix-streaming-ita/",
                        "",
                        "altadefinizione",
                        "Alta Definizione"),
                new StreamCompleteDetails(
                        "matrix revolutions",
                        "https://altadefinizione.style/matrix-revolutions-stream/",
                        "",
                        "altadefinizione",
                        "Alta Definizione")
        );
    }

    static class MockWebsite extends AltaDefinizione {
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