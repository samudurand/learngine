package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.WebsiteCrawlingException;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class FiveMoviesCrawler extends HeadlessCrawler {

    public FiveMoviesCrawler(FiveMovies website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) throws IOException {
        return Mono.<HtmlPage>fromCallable(() -> getOrCreateClient().getPage(buildSearchUrl(title)))
                .flatMapMany(this::findAndParseResults)
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/movie/search/%s", website.getUrl(), encodeSearchParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults(HtmlPage page) {
        return findResultHtmlElementsInPage(page)
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
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
