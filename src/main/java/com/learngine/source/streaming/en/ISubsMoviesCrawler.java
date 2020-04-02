package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import com.learngine.source.utils.UrlFormatter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.alternativeEncodeSearchParams;

@Component
class ISubsMoviesCrawler extends HeadlessCrawler {

    public ISubsMoviesCrawler(ISubsMovies website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) throws IOException {
        var searchPageUrl = String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title));
        var resultsPage = Mono.<HtmlPage>fromCallable(() -> getOrCreateClient().getPage(searchPageUrl));
        return findAndParseResults(resultsPage);
    }

    private Flux<StreamCompleteDetails> findAndParseResults(Mono<HtmlPage> page) {
        return page
                .flatMapMany(this::findResultHtmlElementsInPage)
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<HtmlElement> findResultHtmlElementsInPage(HtmlPage page) {
        return Flux.fromIterable(page.getByXPath("//figcaption"));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(HtmlElement elt) {
        var title = ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent();
        var streamLink = UrlFormatter.generateFullLink(website.getUrl(),
                ((HtmlAnchor) elt.getFirstByXPath(".//parent::figure/parent::a")).getHrefAttribute());
        var imgLink = UrlFormatter.generateFullLink(website.getUrl(),
                ((HtmlImage) elt.getFirstByXPath(".//parent::figure//img")).getSrcAttribute());
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
