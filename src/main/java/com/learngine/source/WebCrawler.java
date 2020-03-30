package com.learngine.source;

import com.learngine.common.Language;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamDetails;
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

    public ParallelFlux<StreamDetails> search(String movieTitle, Language audio, Language subtitles, Boolean includeSearchEngines) {
        return findCompatibleSources(audio)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(source -> {
                    if (source.getWebsite() instanceof SearchEngine && !includeSearchEngines) {
                        return Flux.fromIterable(new ArrayList<>());
                    }
                    return Flux.fromIterable(performSearch(movieTitle, source));
                });
    }

    private List<StreamDetails> performSearch(String movieTitle, WebsiteCrawler crawler) {
        try {
            var results = crawler.searchTitleByName(movieTitle);
            crawler.closeClient();
            return results;
        } catch (Exception ex) {
            log.error("An exception occurred during the search on website " + crawler.getWebsite().getName(), ex);
            crawler.closeClient();
            return new ArrayList<>();
        }
    }

    private Flux<WebsiteCrawler> findCompatibleSources(Language audio) {
        return Flux.fromIterable(streamingSources)
                .filter(crawler -> crawler.getWebsite() instanceof SearchEngine
                        || crawler.getWebsite().getAudioLanguage().equals(audio));
    }
}
