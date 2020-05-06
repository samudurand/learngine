package com.learngine;

import com.learngine.common.Language;
import com.learngine.crawler.WebsiteCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.BiConsumer;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

//TODO reformat to move some of the logic to a StreamingService, and separate StreamingDetails into a Stream and StreamDetails for instance to isolate layers (do not explicitely use is and name in the website result)
@Slf4j
@Service
@Scope(value = SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MultiWebsiteParallelCrawler {

    private final List<WebsiteCrawler> streamingSources;

    @Autowired
    public MultiWebsiteParallelCrawler(final List<WebsiteCrawler> streamingSources) {
        this.streamingSources = streamingSources;
    }

    public ParallelFlux<StreamCompleteDetails> search(String movieTitle, Language audio) {
        return Flux.fromIterable(streamingSources)
                .filter(crawler -> crawler.provideStreamsIn(audio))
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(source -> performSearchThenCloseClients(movieTitle, source));
    }

    private Flux<StreamCompleteDetails> performSearchThenCloseClients(
            final String movieTitle, final WebsiteCrawler crawler) {
        try {
            return crawler.performSearchAndParseResults(movieTitle)
                    .onErrorContinue(logErrorForSingleResult(crawler))
                    .doOnComplete(crawler::closeClient);
        } catch (Exception ex) {
            return logAndBypassErrorForSingleCrawler(crawler, ex);
        }
    }

    private BiConsumer<Throwable, Object> logErrorForSingleResult(WebsiteCrawler crawler) {
        return (ex, stream) -> {
            log.error(
                    String.format("An exception occurred during the processing of a result from website %s : %s",
                            crawler.getWebsite().getName(), stream)
                    , ex
            );
        };
    }

    private Flux<StreamCompleteDetails> logAndBypassErrorForSingleCrawler(WebsiteCrawler crawler, Exception ex) {
        log.error("An exception occurred during the search on website " + crawler.getWebsite().getName(), ex);
        crawler.closeClient();
        return Flux.empty();
    }
}
