package com.learngine.source.streaming.engine;

import com.learngine.WebsiteCrawlingException;
import com.learngine.config.SearchFailedException;
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

import static com.learngine.source.utils.HttpUtils.alternativeEncodeSearchParams;
import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

/**
 * Netflix search engine, currenlty not disabled.
 * The main reason to disable it is that it is not testable with a simple wiremock mock (a lot of js involved to get access to the link).
 */
//@Component
public class UnogsCrawler extends UICrawler {

    public UnogsCrawler(Unogs website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected Flux<StreamCompleteDetails> performSearchAndParseResults(String title) {
        getOrCreateBrowser().get(buildSearchUrl(title));
        return findAndParseResults()
                .onErrorMap(Exception.class, (e) -> new WebsiteCrawlingException(website, e));
    }

    private String buildSearchUrl(String title) {
        return String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title));
    }

    private Flux<StreamCompleteDetails> findAndParseResults() {
        return findResultHtmlElementsInPage()
                .map(this::extractStreamDataFromHtmlElement)
                .map(htmlData -> new StreamCompleteDetails(htmlData, website));
    }

    private Flux<WebElement> findResultHtmlElementsInPage() {
        return Flux.fromIterable(getOrCreateBrowser().findElements(By.className("videodiv")));
    }

    private StreamHtmlParsedData extractStreamDataFromHtmlElement(WebElement elt) {
        var title = elt.findElement(By.tagName("b")).getText();

        var unogLink = elt.findElement(By.xpath(".//parent::a")).getAttribute("href");
        var streamLink = convertNetflixURL(unogLink);

        var imgLink = isImageRetrievable() ? elt.findElement(By.tagName("img")).getAttribute("src") : "";
        return new StreamHtmlParsedData(title, streamLink, imgLink);
    }

    @Override
    protected void performSearch(String title) {
        try {
            // Prevent failed loading of the main page
            getOrCreateBrowser().get("http://localhost");
            navigateToWebsite();

            // Attempt to handle ads redirection
            //if (!browser.getCurrentUrl().contains(website.getUrl())) {
            //    navigateToWebsite();
            //}

            // Wait a little to leave some time to the JS to execute
            Thread.sleep(2000);

            var searchTextField = getOrCreateBrowser().findElement(By.id("atitle"));
            var searchButton = getOrCreateBrowser().findElement(By.id("asfbutton"));
            searchTextField.sendKeys(title);
            searchButton.click();

        } catch (Exception e) {
            throw new SearchFailedException(e);
        }
    }

    @Override
    protected List<StreamCompleteDetails> parseResults() {
        return getOrCreateBrowser().findElements(By.className("videodiv"))
                .stream()
                .map(elt -> new StreamCompleteDetails(
                        elt.findElement(By.tagName("b")).getText(),
                        convertNetflixURL(elt.findElement(By.xpath(".//parent::a")).getAttribute("href")),
                        isImageRetrievable() ? elt.findElement(By.tagName("img")).getAttribute("src") : "",
                        website.getId(),
                        website.getName()
                ))
                .collect(Collectors.toList());
    }

    private String convertNetflixURL(String unogLink) {
        final var urlParts = unogLink.split("=");
        final var netflixId = urlParts[urlParts.length - 1];
        return String.format(Unogs.netflixUrlPattern, netflixId);
    }
}
