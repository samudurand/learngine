package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.crawler.UICrawler;
import com.learngine.exception.WebsiteCrawlingException;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

import static com.learngine.source.utils.HttpUtils.encodeRequestParams;
import static com.learngine.source.utils.HttpUtils.encodeUrlPathParams;

@Slf4j
@Component
public class SolarMovieCrawler extends UICrawler {

    public SolarMovieCrawler(SolarMovie website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        getOrCreateBrowser().get(buildSearchUrl(title));
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> {
                    log.error("Error while searching on " + website.getName() + " : " + e.getMessage(), e);
                    return new WebsiteCrawlingException(website, e);
                });
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/movie/search/%s.html", website.getUrl(), encodeRequestParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getOrCreateBrowser().findElements(By.xpath("//div[@class='ml-item']")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var title = elt.findElement(By.xpath(".//h3")).getText();
        var streamLink = elt.findElement(By.xpath(".//a")).getAttribute("href");
        var imgLink = elt.findElement(By.xpath(".//img")).getAttribute("src");
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }
}
