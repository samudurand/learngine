package com.learngine.source.streaming;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamCompleteDetailsTest {

    @Test
    public void buildStreamDetailsFromHtmlAndWebsiteData() {
        var website = new TestWebsite();
        var htmlData = new StreamHtmlParsedData(" Matrix \t Reloaded", "http://link/to/stream", "http://link/to/img");

        var expected = new StreamCompleteDetails("matrix reloaded", "http://link/to/stream", "http://link/to/img", "testid", "test name");
        assertEquals(expected, new StreamCompleteDetails(htmlData, website));
    }

    private static class TestWebsite implements Website {

        @Override
        public String getId() {
            return "testid";
        }

        @Override
        public String getName() {
            return "test name";
        }

        @Override
        public String getUrl() {
            return "http://example.com";
        }

        @Override
        public Language getAudioLanguage() {
            return Language.ENGLISH;
        }
    }

}