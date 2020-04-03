package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

public interface WebsiteCrawler {

    Website getWebsite();

    Flux<StreamCompleteDetails> performSearchAndParseResults(String title);

    void closeClient();
}
