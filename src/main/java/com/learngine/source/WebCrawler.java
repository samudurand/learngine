package com.learngine.source;

import com.learngine.common.Language;
import com.learngine.source.htmlunit.HtmlUnitBrowsable;
import com.learngine.source.selenium.SeleniumBrowsable;
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

    private final List<Website> streamingSources;

    @Autowired
    public WebCrawler(final List<Website> streamingSources) {
        this.streamingSources = streamingSources;
    }

    public ParallelFlux<StreamDetails> search(String movieTitle, Language audio, Language subtitles, Boolean includeSearchEngines) {
        return findCompatibleSources(audio)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(website -> {
                    log.info("Start search for " + website.getName());
                    if (website instanceof SearchEngine && !includeSearchEngines) {
                        return Flux.fromIterable(new ArrayList<>());
                    }
                    if (website instanceof HtmlUnitBrowsable) {
                        return Flux.fromIterable(performHeadlessSearch(movieTitle, website));
                    } else {
                        return Flux.fromIterable(performBrowserSearch(movieTitle, website));
                    }
                });
    }

    private List<StreamDetails> performBrowserSearch(String movieTitle, Website website) {
        var handler = ((SeleniumBrowsable) website).getHandler();
        try {
            var results = handler.searchTitleByName(movieTitle);
            handler.closeClient();
            return results;
        } catch (Exception ex) {
            log.error("An exception occurred during the search on website " + website.getName(), ex);
            handler.closeClient();
            return new ArrayList<>();
        }
    }

    private List<StreamDetails> performHeadlessSearch(String movieTitle, Website website) {
        var handler = ((HtmlUnitBrowsable) website).getHandler();
        try {
            var results = handler.searchStreamByTitle(movieTitle);
            handler.closeClient();
            return results;
        } catch (Exception ex) {
            log.error("An exception occurred during the search on website " + website.getName(), ex);
            handler.closeClient();
            return new ArrayList<>();
        }
    }

    private Flux<Website> findCompatibleSources(Language audio) {
        return Flux.fromIterable(streamingSources)
                .filter(website -> website instanceof SearchEngine || website.getAudioLanguage().equals(audio));
    }

//    private List<StreamDetails> findMatchingTitles(String searchedName, List<StreamDetails> titlesFound) {
//        return titlesFound.stream()
//                .filter(details -> details.getTitle().toLowerCase().trim().contains(searchedName))
//                .collect(Collectors.toList());
//    }
}
