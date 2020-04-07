package com.learngine.source.streaming.fr;

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

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * This is not using the "Search" feature of this website,
 * since submitting the search only works if the title is exactly formatted the same way on this website (casing included).
 * However, the autocomplete feature on the search box is more flexible, for this reason we are only typing the searched name without submission.
 */
@Component
@Scope(value = SCOPE_PROTOTYPE)
class FilmFraCrawler extends UICrawler {

    public FilmFraCrawler(FilmFra website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    public Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        performSearch(title);
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(getWebsite(), e));
    }

    protected void performSearch(String title) {
        navigateToWebsite();
        var searchTextField = getBrowser().findElement(By.id("tags"));
        searchTextField.sendKeys(title);
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, getWebsite()));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getBrowser()
                .findElement(By.id("ui-id-1"))
                .findElements(By.tagName("li")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var title = elt.findElement(By.tagName("a")).getText();
        var streamLink = getWebsite().getUrl();
        return new StreamHtmlParsedData(title, streamLink, "");
    }
}
