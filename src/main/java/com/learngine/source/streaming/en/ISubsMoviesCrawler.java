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
import com.learngine.source.utils.UrlFormatter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeUrlPathParams;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(value = SCOPE_PROTOTYPE)
@ConditionalOnProperty(value="streaming.isubsmovies.enabled", havingValue = "true")
class ISubsMoviesCrawler extends HeadlessCrawler {

    public ISubsMoviesCrawler(ISubsMovies website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        return Mono.<HtmlPage>fromCallable(() -> getOrCreateClient().getPage(buildSearchUrl(title)))
                .flatMapMany(this::findAndParseResults)
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(getWebsite(), e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/search/%s", getWebsite().getUrl(), encodeUrlPathParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults(HtmlPage page) {
        return findResultHtmlElementsInPage(page)
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, getWebsite()));
    }

    private Flux<HtmlElement> findResultHtmlElementsInPage(HtmlPage page) {
        return Flux.fromIterable(page.getByXPath("//figcaption"));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(HtmlElement elt) {
        var title = ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent();
        var streamLink = UrlFormatter.generateFullLink(getWebsite().getUrl(),
                ((HtmlAnchor) elt.getFirstByXPath(".//parent::figure/parent::a")).getHrefAttribute());
        var imgLink = UrlFormatter.generateFullLink(getWebsite().getUrl(),
                ((HtmlImage) elt.getFirstByXPath(".//parent::figure//img")).getSrcAttribute());
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
