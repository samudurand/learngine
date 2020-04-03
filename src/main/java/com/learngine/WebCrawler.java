package com.learngine;

import com.learngine.common.Language;
import com.learngine.crawler.WebsiteCrawler;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamCompleteDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

//TODO reformat to move some of the logic to a StreamingService, and separate StreamingDetails into a Stream and StreamDetails for instance to isolate layers (do not explicitely use is and name in the website result)
@Service
@Slf4j
public class WebCrawler {

    private final List<WebsiteCrawler> streamingSources;

    @Autowired
    public WebCrawler(final List<WebsiteCrawler> streamingSources) {
        this.streamingSources = streamingSources;
    }

    public ParallelFlux<StreamCompleteDetails> search(String movieTitle, Language audio, Language subtitles, Boolean includeSearchEngines) {
        return findCompatibleSources(audio)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(source -> {
                    if (source.getWebsite() instanceof SearchEngine && !includeSearchEngines) {
                        return Flux.fromIterable(new ArrayList<>());
                    }
                    return performSearch(movieTitle, source);
                });
    }

    private Flux<StreamCompleteDetails> performSearch(String movieTitle, WebsiteCrawler crawler) {
        try {
            var results = crawler.performSearchAndParseResults(movieTitle);
            crawler.closeClient();
            return results;
        } catch (Exception ex) {
            log.error("An exception occurred during the search on website " + crawler.getWebsite().getName(), ex);
            crawler.closeClient();
            return Flux.empty();
        }
    }

    private Flux<WebsiteCrawler> findCompatibleSources(Language audio) {
        return Flux.fromIterable(streamingSources)
                .filter(crawler -> crawler.getWebsite() instanceof SearchEngine
                        || crawler.getWebsite().getAudioLanguage().equals(audio));
    }
}
