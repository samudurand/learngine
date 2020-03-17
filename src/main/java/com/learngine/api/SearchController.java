package com.learngine.api;

import com.learngine.api.domain.MovieSummary;
import com.learngine.common.Language;
import com.learngine.source.WebCrawler;
import com.learngine.source.metadata.MetadataService;
import com.learngine.source.streaming.StreamDetails;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class SearchController {

    private WebCrawler crawler;
    private MetadataService metadataService;

    public SearchController(WebCrawler crawler, MetadataService metadataService) {
        this.crawler = crawler;
        this.metadataService = metadataService;
    }

    @GetMapping("/search/movies")
    public Flux<MovieSummary> searchMatchingMovies(
            @RequestParam @NotBlank String title
    ) {
        return metadataService.findMatchingMovies(title);
    }

    @GetMapping(value = "/search/streams", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @GetMapping(value="/search/streams")
    public ParallelFlux<StreamDetails> searchForTitle(
            @RequestParam @NotNull String title,
            @RequestParam(defaultValue = "en") Language audio,
            @RequestParam(required = false) Language subtitles,
            @RequestParam(required = false, defaultValue = "false") Boolean engines
    ) {
        return crawler.search(title, audio, subtitles, engines);
    }

    @GetMapping(value = "/search/alternatives", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> searchForTitle(
            @RequestParam @NotNull String title,
            @RequestParam @NotNull Integer movieId,
            @RequestParam(defaultValue = "en") Language audio
    ) {
        return metadataService.findLocalizedTitles(title, movieId, audio);
    }

}
