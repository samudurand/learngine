package com.learngine.source.streaming.fr;

import com.learngine.WebsiteCrawlingException;
import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import com.learngine.source.streaming.StreamHtmlParsedData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

/**
 * This is not using the "Search" feature of this website,
 * since submitting the search only works if the title is exactly formatted the same way on this website (casing included).
 * However, the autocomplete feature on the search box is more flexible, for this reason we are only typing the searched name without submission.
 */
@Component
class FilmFraCrawler extends UICrawler {

    public FilmFraCrawler(FilmFra website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        performSearch(title);
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    protected void performSearch(String title) {
        navigateToWebsite();
        var searchTextField = getOrCreateBrowser().findElement(By.id("tags"));
        searchTextField.sendKeys(title);
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getOrCreateBrowser()
                .findElement(By.id("ui-id-1"))
                .findElements(By.tagName("li")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var title = elt.findElement(By.tagName("a")).getText();
        var streamLink = website.getUrl();
        return new StreamHtmlParsedData(title, streamLink, "");
    }
}
