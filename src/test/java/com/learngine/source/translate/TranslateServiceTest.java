package com.learngine.source.translate;

import com.learngine.api.model.MovieSummary;
import com.learngine.source.metadata.MetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslateServiceTest {

    @Mock
    private GoogleCloudClient client;
    @Mock
    private MetadataService metadataService;

    private TranslateService service;

    @BeforeEach
    public void setup() {
         service = new TranslateService(client, metadataService);
    }

    @Test
    public void translateFromSimpleCode() {
        when(metadataService.getMovieDetails(603))
                .thenReturn(Mono.just(new MovieSummary(603, "", "", null, "hello", 0f)));
        when(client.translateFromEnglish("hello", "it")).thenReturn(Mono.just("ciao"));

        service.translateDescription(603, "it").block();

        verify(client).translateFromEnglish("hello", "it");
    }

    @Test
    public void translateFromComposedCodeUsingNormalizedCode() {
        when(metadataService.getMovieDetails(603))
                .thenReturn(Mono.just(new MovieSummary(603, "", "", null, "hello", 0f)));
        when(client.translateFromEnglish("hello", "it")).thenReturn(Mono.just("ciao"));

        service.translateDescription(603, "it-IT").block();

        verify(client).translateFromEnglish("hello", "it");
    }
}