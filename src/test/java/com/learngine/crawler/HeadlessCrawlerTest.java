package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class HeadlessCrawlerTest {

    private HeadlessCrawler crawler;
    private Supplier<WebClient> clientSupplier = () -> mock(WebClient.class);

    @BeforeEach
    void setUp() {
        crawler = new HeadlessCrawler(mock(Website.class), clientSupplier) {
            @Override
            public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
                return null;
            }
        };
    }

    @Test
    void createANewClient() {
        var expectedResult = crawler.getOrCreateClient();
        assertNotNull(expectedResult);
    }

    @Test
    void useExistingClient() {
        var client1 = crawler.getOrCreateClient();
        var client2 = crawler.getOrCreateClient();

        assertEquals(client1, client2);
    }
}