package com.learngine.source;

import com.learngine.api.domain.StreamsSearchResults;
import com.learngine.common.Language;
import com.learngine.source.htmlunit.HtmlUnitBrowsable;
import com.learngine.source.metadata.MetadataService;
import com.learngine.source.selenium.SeleniumBrowsable;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebCrawler {

    private final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final MetadataService metadataSource;
    private final List<Website> streamingSources;

    @Autowired
    public WebCrawler(final MetadataService metadataSource, List<Website> streamingSources) {
        this.metadataSource = metadataSource;
        this.streamingSources = streamingSources;
    }

    public StreamsSearchResults search(String movieTitle, Integer movieId, Language audio, Language subtitles) {
        final var alternativeTitles = metadataSource.findLocalizedTitles(movieTitle, movieId, audio);
        final var compatibleSources = findCompatibleSources(audio);

        var streams = compatibleSources.parallelStream()
                .map(website -> {
                    if (website instanceof HtmlUnitBrowsable) {
                        return performHeadlessSearch(movieTitle, website);
                    } else {
                        return performBrowserSearch(movieTitle, website);
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new StreamsSearchResults(streams, alternativeTitles);
    }

    private List<StreamDetails> performBrowserSearch(String movieTitle, Website website) {
        var handler = ((SeleniumBrowsable) website).getHandler();
        try {
            var results = handler.searchTitleByName(movieTitle);
            handler.closeClient();
            return results;
        } catch (Exception ex) {
            logger.error("An exception occurred during the search on website " + website.getName(), ex);
            handler.closeClient();
            return new ArrayList<StreamDetails>();
        }
    }

    private List<StreamDetails> performHeadlessSearch(String movieTitle, Website website) {
        var handler = ((HtmlUnitBrowsable) website).getHandler();
        try {
            var results = handler.searchTitleByName(movieTitle);
            handler.closeClient();
            return results;
        } catch (Exception ex) {
            logger.error("An exception occurred during the search on website " + website.getName(), ex);
            handler.closeClient();
            return new ArrayList<StreamDetails>();
        }
    }

    private List<Website> findCompatibleSources(Language audio) {
        return streamingSources.stream()
                .filter(website -> website instanceof SearchEngine || website.getAudioLanguage().equals(audio))
                .collect(Collectors.toList());
    }

//    private List<StreamDetails> findMatchingTitles(String searchedName, List<StreamDetails> titlesFound) {
//        return titlesFound.stream()
//                .filter(details -> details.getTitle().toLowerCase().trim().contains(searchedName))
//                .collect(Collectors.toList());
//    }
}
