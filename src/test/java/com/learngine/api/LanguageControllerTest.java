package com.learngine.api;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.learngine.api.model.TextToTranslate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class LanguageControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void translateFromEnglishToItalian() throws Exception {
        client.post()
                .uri("/languages/translate")
                .body(BodyInserters.fromValue(new TextToTranslate("hello", "it")))
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .json("{\"translation\":\"Ciao\"}");
    }

    @TestConfiguration
    static class GoogleTestClient {
        @Bean
        @Primary
        public Translate translate() {
            var mockService = mock(Translate.class);
            var mockTranslation = mock(Translation.class);
            when(mockTranslation.getTranslatedText()).thenReturn("Ciao");
            when(mockService.translate(anyString(), any(), any())).thenReturn(mockTranslation);
            return mockService;
        }
    }
}