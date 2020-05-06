package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeRequestParams;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(value = SCOPE_PROTOTYPE)
@Slf4j
@ConditionalOnProperty(value="streaming.fivemovies.enabled", havingValue = "true")
public class FiveMoviesCrawler extends HeadlessCrawler {

    public FiveMoviesCrawler(FiveMovies website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        return Mono.<HtmlPage>fromCallable(() -> getOrCreateClient().getPage(buildSearchUrl(title)))
                .onErrorResume((e) -> {
                    log.error("Unable to process results from {}", getWebsite().getName());
                    return Mono.empty();
                })
                .flatMapMany(this::findAndParseResults);
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/movie/search/%s", getWebsite().getUrl(), encodeRequestParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults(HtmlPage page) {
        return findResultHtmlElementsInPage(page)
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, getWebsite()))
                .onErrorContinue((e, obj) -> log.error("Unable to process {}", obj));
    }

    private Flux<HtmlElement> findResultHtmlElementsInPage(HtmlPage page) {
        return Flux.fromIterable(page.getByXPath("//div[@class='ml-item']"));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(HtmlElement elt) {
        var title = ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent();
        var streamLink = ((HtmlAnchor) elt.getFirstByXPath(".//a")).getHrefAttribute();
        var imgLink = ((HtmlImage) elt.getFirstByXPath(".//a//img")).getAttribute("data-original");
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
