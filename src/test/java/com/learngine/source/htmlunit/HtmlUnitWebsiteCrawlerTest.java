package com.learngine.source.htmlunit;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HtmlUnitWebsiteCrawlerTest {

    private final static StreamDetails matrixStream = getMatrixStreamDetails();

    @Mock
    private Website matrixStreamingWebsite;

    HtmlUnitWebsiteCrawler crawler;
    @BeforeEach
    void setUp() {
        crawler = Mockito.mock(
                HtmlUnitWebsiteCrawler.class,
                Mockito.withSettings()
                        .useConstructor(matrixStreamingWebsite,
                                new HtmlUnitConfig().defaultWebClient()));
        when(crawler.searchTitleByName(any())).thenCallRealMethod();
    }

    @Test
    void searchByTitlePerformsSearchThenParseResults() throws IOException {
        var searchResultPage = Mockito.mock(HtmlPage.class);
        when(crawler.performSearch("matrix")).thenReturn(searchResultPage);
        var expectedResults = List.of(matrixStream);
        when(crawler.parseResults(searchResultPage)).thenReturn(expectedResults);

        var result = crawler.searchTitleByName("matrix");

        Mockito.verify(crawler).performSearch("matrix");
        Mockito.verify(crawler).parseResults(searchResultPage);
        assertIterableEquals(expectedResults, result);
    }

    @Test
    void searchByTitleFails() throws IOException {
        when(crawler.performSearch(any())).thenThrow(new IOException("Search failed"));

        assertThrows(SearchFailedException.class, () -> crawler.searchTitleByName("matrix"));
    }

    @Test
    @Disabled
    void addWebsiteBaseDomainToRelativeLink() {

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