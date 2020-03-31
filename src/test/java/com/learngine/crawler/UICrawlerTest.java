package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

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

class UICrawlerTest {

    private final static StreamDetails matrixStream = getMatrixStreamDetails();
    UICrawler crawler;
    @Mock
    private Website matrixStreamingWebsite;
    private Supplier<WebDriver> browserSupplier = () -> mock(WebDriver.class);

    private static StreamDetails getMatrixStreamDetails() {
        return new StreamDetails(
                "The Matrix",
                "https://example/thematrix.html",
                "http://example/matrix.jpg",
                "loveMatrix",
                "Love Matrix");
    }

    @BeforeEach
    void setUp() {
        crawler = spy(new UICrawler(matrixStreamingWebsite, browserSupplier) {
        });
    }

    @Test
    void searchByTitlePerformsSearchThenParseResults() throws IOException {
        doNothing().when(crawler).performSearch("matrix");
        var expectedResults = List.of(matrixStream);
        doReturn(expectedResults).when(crawler).parseResults();

        var result = crawler.searchTitleByName("matrix");

        Mockito.verify(crawler).performSearch("matrix");
        Mockito.verify(crawler).parseResults();
        assertIterableEquals(expectedResults, result);
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