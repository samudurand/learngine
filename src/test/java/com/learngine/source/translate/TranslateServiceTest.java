package com.learngine.source.translate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TranslateServiceTest {

    @Mock
    private GoogleCloudClient client;

    private TranslateService service;

    @BeforeEach
    public void setup() {
         service = new TranslateService(client);
    }

    @Test
    public void translateFromSimpleCode() {
        service.translate("hello", "it");
        verify(client).translateFromEnglish("hello", "it");
    }

    @Test
    public void translateFromComposedCodeUsingNormalizedCode() {
        service.translate("hello", "es-PR");
        verify(client).translateFromEnglish("hello", "es");
    }
}