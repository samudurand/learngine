package com.learngine;

import com.learngine.common.Language;
import com.learngine.crawler.WebsiteCrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamCompleteDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

//TODO reformat to move some of the logic to a StreamingService, and separate StreamingDetails into a Stream and StreamDetails for instance to isolate layers (do not explicitely use is and name in the website result)
@Service
@Slf4j
@Scope(value=SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
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

    private Flux<StreamCompleteDetails> performSearch(final String movieTitle, final WebsiteCrawler crawler) {
        try {
            var results = crawler.performSearchAndParseResults(movieTitle);
            return results
                    .doOnComplete(crawler::closeClient)
                    .onErrorMap(ex -> {
                log.error("An exception occurred during the search on website " + crawler.getWebsite().getName(), ex);
                crawler.closeClient();
                return new WebsiteCrawlingException(crawler.getWebsite(), ex);
            });
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
