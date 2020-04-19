package com.learngine.api;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
import com.learngine.source.streaming.SeleniumTest;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(WireMockExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
class SearchControllerSearchStreamTest implements SeleniumTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().port(SeleniumTest.EXPECTED_WIREMOCK_PORT));

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        stubFor(get(anyUrl()).willReturn(notFound()));
    }

    @Test
    public void peformSearchWithTitleMatrixAndDefaultAudioEnglish() throws Exception {
        stubEnglishAllSources();

        var results = new HashSet<StreamCompleteDetails>();
        client.get()
                .uri("/search/streams?title=matrix")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(StreamCompleteDetails.class)
                .getResponseBody()
                .toIterable()
                .forEach(results::add);

        assertEquals(englishMatrixSearchResults().size(), results.size());
        assertEquals(englishMatrixSearchResults(), results);
    }

    @Test
    public void peformSearchWithoutTitleFails() throws Exception {
        client.get()
                .uri("/search/streams")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void peformSearchWithTitleMatrixAndAudioItalian() throws Exception {
        stubItalianSources();

        var results = new HashSet<StreamCompleteDetails>();
        client.get()
                .uri("/search/streams?title=matrix&audio=it")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(StreamCompleteDetails.class)
                .getResponseBody()
                .toIterable()
                .forEach(results::add);

        assertEquals(italianMatrixSearchResults(), results);
    }

    private Set<StreamCompleteDetails> italianMatrixSearchResults() {
        return Set.of(
                new StreamCompleteDetails(
                        "matrix reloaded",
                        "https://altadefinizione.style/matrix-reloaded-streaming-ita/",
                        "",
                        "altadefinizione",
                        "Alta Definizione",
                        ""),
                new StreamCompleteDetails(
                        "matrix",
                        "https://altadefinizione.style/matrix-streaming-ita/",
                        "",
                        "altadefinizione",
                        "Alta Definizione",
                        ""),
                new StreamCompleteDetails(
                        "matrix revolutions",
                        "https://altadefinizione.style/matrix-revolutions-stream/",
                        "",
                        "altadefinizione",
                        "Alta Definizione",
                        "")
        );
    }

    private Set<StreamCompleteDetails> englishMatrixSearchResults() {
        return Set.of(
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
                        ""),
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


    private void stubEnglishAllSources() throws IOException {
        String fiveMoviesHtml = FileUtils.readFile("/html/5movies/matrix.html");
        stubFor(get("/5movies/movie/search/matrix").willReturn(aResponse().withBody(fiveMoviesHtml)));
        String solarmovieHtml = FileUtils.readFile("/html/solarmovie/matrix.html");
        stubFor(get("/solarmovie/movie/search/matrix.html").willReturn(aResponse().withBody(solarmovieHtml)));
        stubFor(get("/isubsmovies/search/matrix").willReturn(aResponse().withBody("<html></html>")));
    }

    private void stubItalianSources() throws IOException {
        String altadHtml = FileUtils.readFile("/html/altadefinizione/matrix.html");
        stubFor(get(urlEqualTo("/altadefinizione?s=matrix")).willReturn(aResponse().withBody(altadHtml)));
        stubFor(get(urlEqualTo("/anime-altadefinizione?s=matrix")).willReturn(aResponse().withBody("<html></html>")));
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public Supplier<WebDriver> testDriver() {
            return config.remoteBrowser(seleniumNode.getSeleniumAddress().toString(), config.browserOptions());
        }
    }

}