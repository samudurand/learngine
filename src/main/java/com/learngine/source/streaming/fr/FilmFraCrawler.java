package com.learngine.source.streaming.fr;

import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
class FilmFraCrawler extends UICrawler {

    public FilmFraCrawler(Supplier<WebDriver> browserSupplier) {
        super(new FilmFra(), browserSupplier);
    }

    /**
     * Submitting the search only works if the title is exactly formatted the same way on this website (casing included).
     * However the autocomplete feature on the search box is more flexible, for this reason we are only typing the searched name without submission.
     */
    @Override
    protected void performSearch(String title) {
        navigateToWebsite();
        var searchTextField = getOrCreateBrowser().findElement(By.id("tags"));
        searchTextField.sendKeys(title);
    }

    @Override
    protected List<StreamDetails> parseResults() {
        return getOrCreateBrowser().findElement(By.id("ui-id-1")).findElements(By.tagName("li"))
                .stream()
                .map(elt -> {
                    var link = elt.findElement(By.tagName("a"));
                    return new StreamDetails(
                            link.getText(),
                            website.getUrl(),
                            "", // No direct results so no images
                            website.getId(),
                            website.getName());
                })
                .collect(Collectors.toList());
    }
}
