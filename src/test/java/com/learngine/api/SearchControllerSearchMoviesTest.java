package com.learngine.api;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.learngine.FileUtils;
import com.learngine.source.streaming.SeleniumTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(WireMockExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
class SearchControllerSearchMoviesTest {

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().port(SeleniumTest.EXPECTED_WIREMOCK_PORT));

    @Autowired
    private WebTestClient client;

    @Test
    public void searchForMoviesWithTitleMatrix() throws Exception {
        String expectedJson = FileUtils.readFile("/json/search_movies_matrix.json");

        client.get()
                .uri("/search/movies?title=matrix")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json(expectedJson);
    }

    @Test
    public void searchForSecondPageOfMoviesWithTitleMatrix() throws Exception {
        String expectedJson = FileUtils.readFile("/json/search_movies_matrix_p2.json");

        client.get()
                .uri("/search/movies?title=matrix&page=2")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json(expectedJson);
    }

    @Test
    public void searchForMoviesWithEmptyTitleFail() throws Exception {
        client.get()
                .uri("/search/movies?title=")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().is5xxServerError(); // TODO this isn't right, should be 400
    }

    @Test
    public void searchForMoviesWithoutTitleFail() throws Exception {
        client.get()
                .uri("/search/movies")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isBadRequest();
    }
}