package com.learngine.source.streaming.it;

import com.learngine.WebsiteCrawlingException;
import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizioneCrawler extends UICrawler {

    public AltaDefinizioneCrawler(AltaDefinizione website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        getOrCreateBrowser().get(buildSearchUrl(title));
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title));
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
