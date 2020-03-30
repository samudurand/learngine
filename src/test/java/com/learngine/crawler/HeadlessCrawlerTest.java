package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.config.SearchFailedException;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeadlessCrawlerTest {

    private final static StreamDetails matrixStream = getMatrixStreamDetails();

    @Mock
    private Website matrixStreamingWebsite;

    private Supplier<WebClient> clientSupplier = () -> mock(WebClient.class);

    HeadlessCrawler crawler;

    @BeforeEach
    void setUp() {
        crawler = spy(new HeadlessCrawler(matrixStreamingWebsite, clientSupplier) {});
    }

    @Test
    void searchByTitlePerformsSearchThenParseResults() throws IOException {
        var searchResultPage = Mockito.mock(HtmlPage.class);
        doReturn(searchResultPage).when(crawler).performSearch("matrix");
        var expectedResults = List.of(matrixStream);
        doReturn(expectedResults).when(crawler).parseResults(searchResultPage);

        var result = crawler.searchTitleByName("matrix");

        Mockito.verify(crawler).performSearch("matrix");
        Mockito.verify(crawler).parseResults(searchResultPage);
        assertIterableEquals(expectedResults, result);
    }

    @Test
    void searchByTitleFails() throws IOException {
        doThrow(new IOException("Search failed")).when(crawler).performSearch(any());
        assertThrows(SearchFailedException.class, () -> crawler.searchTitleByName("matrix"));
    }

    @Test
    void createANewClient() {
        var expectedResult = crawler.getClient();
        assertNotNull(expectedResult);
    }

    @Test
    void useExistingClient() {
        var client1 = crawler.getClient();
        var client2 = crawler.getClient();

        assertEquals(client1, client2);
    }

    private static StreamDetails getMatrixStreamDetails() {
        return new StreamDetails(
                "The Matrix",
                "https://example/thematrix.html",
                "http://example/matrix.jpg",
                "loveMatrix",
                "Love Matrix");
    }
}