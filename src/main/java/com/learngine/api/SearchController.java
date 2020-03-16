package com.learngine.api;

import com.learngine.api.domain.MovieSummary;
import com.learngine.common.Language;
import com.learngine.source.WebCrawler;
import com.learngine.source.metadata.MetadataService;
import com.learngine.source.streaming.StreamDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class SearchController {

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private WebCrawler crawler;
    private MetadataService metadataService;

    public SearchController(WebCrawler crawler, MetadataService metadataService) {
        this.crawler = crawler;
        this.metadataService = metadataService;
    }

    @GetMapping("/search/movies")
    public List<MovieSummary> searchMatchingMovies(
            @RequestParam @NotBlank String title
    ) {
        return metadataService.findMatchingMovies(title);
    }


    @GetMapping(value = "/search/streams", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @GetMapping(value="/search/streams")
    public Flux<StreamDetails> searchForTitle(
            @RequestParam @NotNull String title,
            @RequestParam @NotNull Integer movieId,
            @RequestParam(defaultValue = "en") Language audio,
            @RequestParam(required = false) Language subtitles,
            @RequestParam(required = false, defaultValue = "false") Boolean engines
    ) {
        logger.info("call");
        return Flux.fromStream(crawler.search(title, movieId, audio, subtitles, engines));
    }

}
