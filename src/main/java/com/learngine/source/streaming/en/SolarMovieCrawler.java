package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeUrlPathParams;

@Component
public class SolarMovieCrawler extends HeadlessCrawler {

    public SolarMovieCrawler(SolarMovie website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        return Mono.<HtmlPage>fromCallable(() -> getOrCreateClient().getPage(buildSearchUrl(title)))
                .flatMapMany(this::findAndParseResults)
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/search/%s", website.getUrl(), encodeUrlPathParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults(HtmlPage page) {
        return findResultHtmlElementsInPage(page)
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<HtmlElement> findResultHtmlElementsInPage(HtmlPage page) {
        return Flux.fromIterable(page.getByXPath("//div[@class='result-item']"));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(HtmlElement elt) {
        HtmlAnchor anchor = elt.getFirstByXPath(".//div[@class='title']/a");
        HtmlImage img = elt.getFirstByXPath(".//img");

        var title = anchor.getTextContent();
        var streamLink = anchor.getHrefAttribute();
        var imgLink = img.getSrcAttribute();
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
