package com.learngine.source.streaming.en;

import com.learngine.crawler.UICrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeRequestParams;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(value = SCOPE_PROTOTYPE)
public class SolarMovieCrawler extends UICrawler {

    public SolarMovieCrawler(SolarMovie website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        getBrowser().get(buildSearchUrl(title));
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> {
                    log.error("Error while searching on " + getWebsite().getName() + " : " + e.getMessage(), e);
                    return new WebsiteCrawlingException(getWebsite(), e);
                });
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/movie/search/%s.html", getWebsite().getUrl(), encodeRequestParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, getWebsite()));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getBrowser().findElements(By.xpath("//div[@class='ml-item']")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var title = elt.findElement(By.xpath(".//h3")).getText();
        var streamLink = elt.findElement(By.xpath(".//a")).getAttribute("href");
        var imgLink = elt.findElement(By.xpath(".//img")).getAttribute("src");
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
