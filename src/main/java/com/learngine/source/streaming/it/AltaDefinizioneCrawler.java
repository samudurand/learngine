package com.learngine.source.streaming.it;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.WebsiteCrawlingException;
import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.alternativeEncodeSearchParams;
import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizioneCrawler extends UICrawler {

    public AltaDefinizioneCrawler(AltaDefinizione website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) throws IOException {
            final var searchPageUrl = String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title));
            getOrCreateBrowser().get(searchPageUrl);
            return findAndParseResults()
                    .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getOrCreateBrowser().findElements(By.xpath("//h5[@class='titleFilm']")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var anchor = elt.findElement(By.xpath(".//parent::div/parent::div/parent::a"));
        var img = elt.findElement(By.xpath(".//parent::div/parent::div//img"));

        var title = elt.getText();
        var streamLink = anchor.getAttribute("href");
        var imgLink = isImageRetrievable() ? img.getAttribute("src") : "";
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
