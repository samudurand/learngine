package com.learngine.source.streaming;

import com.learngine.common.Language;
import com.learngine.source.Website;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamCompleteDetailsTest {

    @Test
    public void buildStreamDetailsFromHtmlAndWebsiteData() {
        var website = new TestWebsite();
        var htmlData = new StreamHtmlParsedData(" Matrix \t Reloaded", "http://link/to/stream", "http://link/to/img");

        var expected = new StreamCompleteDetails("matrix reloaded", "http://link/to/stream", "http://link/to/img", "testid", "test name", "http://alt-example.com");
        assertEquals(expected, new StreamCompleteDetails(htmlData, website));
    }

    @Test
    public void buildStreamDetailsFromHtmlAndWebsiteDataWithNoAlternativeUrl() {
        var website = new TestWebsiteNoAltUrl();
        var htmlData = new StreamHtmlParsedData(" Matrix \t Reloaded", "http://link/to/stream", "http://link/to/img");

        var expected = new StreamCompleteDetails("matrix reloaded", "http://link/to/stream", "http://link/to/img", "testid", "test name", "");
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
        public Optional<String> getAlternativeUrl() {
            return Optional.of("http://alt-example.com");
        }

        @Override
        public Language getAudioLanguage() {
            return Language.ENGLISH;
        }
    }

    private static class TestWebsiteNoAltUrl implements Website {

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