package com.learngine.source.streaming.it;

import com.learngine.crawler.UICrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeRequestParams;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(value = SCOPE_PROTOTYPE)
public class AltaDefinizioneCrawler extends UICrawler {

    public AltaDefinizioneCrawler(AltaDefinizione website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        getBrowser().get(buildSearchUrl(title));
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(getWebsite(), e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s?s=%s", getWebsite().getUrl(), encodeRequestParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, getWebsite()));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        var count = getBrowser().findElements(By.xpath("//h5[@class='titleFilm']")).size();
        return Flux.fromIterable(getBrowser().findElements(By.xpath("//h5[@class='titleFilm']")));
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
