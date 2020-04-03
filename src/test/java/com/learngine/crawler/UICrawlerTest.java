package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class UICrawlerTest {

    private UICrawler crawler;
    private Supplier<WebDriver> browserSupplier = () -> mock(WebDriver.class);

    @BeforeEach
    void setUp() {
        crawler = new UICrawler(mock(Website.class), browserSupplier) {
            @Override
            public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
                return null;
            }
        };
    }

    @Test
    void createANewBrowser() {
        var expectedResult = crawler.getOrCreateBrowser();
        assertNotNull(expectedResult);
    }

    @Test
    void useExistingBrowser() {
        var browser1 = crawler.getOrCreateBrowser();
        var browser2 = crawler.getOrCreateBrowser();

        assertEquals(browser1, browser2);
    }
}