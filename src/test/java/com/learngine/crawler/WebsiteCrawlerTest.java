package com.learngine.crawler;

import com.learngine.TestWebsite;
import com.learngine.common.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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