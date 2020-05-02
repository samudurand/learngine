package com.learngine.api;

import com.learngine.MultiWebsiteParallelCrawler;
import com.learngine.common.Language;
import com.learngine.source.metadata.MetadataService;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class SearchController {

    private final MultiWebsiteParallelCrawler crawler;
    private final MetadataService metadataService;

    public SearchController(MultiWebsiteParallelCrawler crawler, MetadataService metadataService) {
        this.crawler = crawler;
        this.metadataService = metadataService;
    }

    @GetMapping("/search/movies")
    public Mono<MovieSearchResult> searchMatchingMovies(
            @RequestParam @NotBlank String title,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        return metadataService.findMatchingMovies(title, page);
    }

    @GetMapping(value = "/search/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<String>> searchForTitle(
            @RequestParam @NotNull Integer movieId,
            @RequestParam(defaultValue = "en") Language audio
    ) {
        return metadataService.findLocalizedTitles(movieId, audio).collectList();
    }

    @GetMapping(value = "/search/streams", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ParallelFlux<StreamCompleteDetails> searchForTitle(
            @RequestParam @NotBlank String title,
            @RequestParam(defaultValue = "en") Language audio
    ) {
        return crawler.search(title, audio);
    }

}
