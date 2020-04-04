package com.learngine;

import com.learngine.common.Language;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.crawler.UICrawler;
import com.learngine.crawler.WebsiteCrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiWebsiteParallelCrawlerTest {

    @Mock
    UICrawler uiCrawler;

    @Mock
    HeadlessCrawler headlessCrawler;

    MultiWebsiteParallelCrawler multiCrawler;

    @BeforeEach
    void setUp() {
        lenient().when(uiCrawler.provideStreamsIn(any())).thenCallRealMethod();
        lenient().when(headlessCrawler.provideStreamsIn(any())).thenCallRealMethod();
    }

    @Test
    public void performSearchForMatrixThenCloseClients() {
        StreamCompleteDetails result1 = setupNewCrawlerWithNewResult(uiCrawler, Language.ENGLISH);
        StreamCompleteDetails result2 = setupNewCrawlerWithNewResult(headlessCrawler, Language.ENGLISH);
        multiCrawler = new MultiWebsiteParallelCrawler(List.of(uiCrawler, headlessCrawler));

        var results = multiCrawler.search("matrix", Language.ENGLISH);

        StepVerifier.create(results)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(streams -> {
                    Assert.assertThat(streams, containsInAnyOrder(result1, result2));
                })
                .verifyComplete();
        verify(uiCrawler, times(1)).closeClient();
        verify(headlessCrawler, times(1)).closeClient();
    }

    @Test
    public void performSearchForMatrixOnlyOnSpanishWebsites() {
        setupNewCrawlerWithNewResult(uiCrawler, Language.ENGLISH);
        StreamCompleteDetails result2 = setupNewCrawlerWithNewResult(headlessCrawler, Language.SPANISH);
        multiCrawler = new MultiWebsiteParallelCrawler(List.of(uiCrawler, headlessCrawler));

        var results = multiCrawler.search("matrix", Language.SPANISH);

        StepVerifier.create(results)
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .consumeRecordedWith(streams -> {
                    assertEquals(result2, List.copyOf(streams).get(0));
                })
                .verifyComplete();
    }

    @Test
    public void performSearchForWithNoCrawlersAndNoResults() {
        multiCrawler = new MultiWebsiteParallelCrawler(List.of());

        var results = multiCrawler.search("matrix", Language.ENGLISH);

        StepVerifier.create(results)
                .verifyComplete();
    }

    @Test
    public void performSearchForWithNoResults() {
        var website = new TestWebsite("website", "Website", Language.ENGLISH);
        when(uiCrawler.getWebsite()).thenReturn(website);
        when(uiCrawler.performSearchAndParseResults("matrix")).thenReturn(Flux.empty());
        multiCrawler = new MultiWebsiteParallelCrawler(List.of(uiCrawler));

        var results = multiCrawler.search("matrix", Language.ENGLISH);

        StepVerifier.create(results)
                .verifyComplete();
    }

    @Test
    public void aSingleCrawlerFailingDoesNotFailTheWholeSearchAndStillCloseClient() {
        setupNewCrawlerWithNewResult(uiCrawler, Language.ENGLISH);
        setupNewCrawlerWithNewResult(headlessCrawler, Language.ENGLISH);
        when(uiCrawler.performSearchAndParseResults("matrix")).thenThrow(WebsiteCrawlingException.class);
        multiCrawler = new MultiWebsiteParallelCrawler(List.of(uiCrawler, headlessCrawler));

        var results = multiCrawler.search("matrix", Language.ENGLISH);

        StepVerifier.create(results)
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .verifyComplete();
        verify(uiCrawler, times(1)).closeClient();
    }

    private StreamCompleteDetails setupNewCrawlerWithNewResult(WebsiteCrawler crawler, Language language) {
        var website1 = new TestWebsite("website", "Website", language);
        var result1 = new StreamCompleteDetails("matrix", "http://link/to/ui", "http://ui/img.jpg", "website1", "Website1", "");
        when(crawler.getWebsite()).thenReturn(website1);
        lenient().when(crawler.performSearchAndParseResults("matrix")).thenReturn(Flux.fromIterable(List.of(result1)));
        return result1;
    }

}