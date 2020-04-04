package com.learngine.crawler;

import com.learngine.TestWebsite;
import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebsiteCrawlerTest {

    @Mock
    private WebsiteCrawler crawler;

    @BeforeEach
    void setUp() {
        when(crawler.provideStreamsIn(any())).thenCallRealMethod();
    }

    @Test
    public void determineIfWebsiteProvidesStreamsInLanguage() {
        var website = new TestWebsite("website", "Website", Language.ENGLISH);
        when(crawler.getWebsite()).thenReturn(website);

        assertTrue(crawler.provideStreamsIn(Language.ENGLISH));
        assertFalse(crawler.provideStreamsIn(Language.SPANISH));
    }
}