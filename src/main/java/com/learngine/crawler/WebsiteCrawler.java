package com.learngine.crawler;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import reactor.core.publisher.Flux;

public interface WebsiteCrawler {

    Website getWebsite();

    Flux<StreamCompleteDetails> performSearchAndParseResults(String title);

    default Boolean provideStreamsIn(Language language) {
        return this.getWebsite().getAudioLanguage().equals(language);
    }

    void closeClient();
}
