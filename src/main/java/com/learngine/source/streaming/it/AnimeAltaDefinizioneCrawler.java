package com.learngine.source.streaming.it;

import com.learngine.source.Website;
import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Slf4j
public class AnimeAltaDefinizioneCrawler extends SeleniumWebsiteCrawler {

    public AnimeAltaDefinizioneCrawler(Website website, WebDriver browser) {
        super(website, browser);
    }

    @Override
    protected void performSearch(String title) {
        browser.get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults() {
        var result = browser.findElements(By.className("article-image"));
        log.info("Found {} elements", result.size());
        return browser.findElements(By.className("article-image"))
                .stream()
                .map(elt -> {
                    var link = elt.findElement(By.tagName("h3")).findElement(By.tagName("a"));
                    var img = elt.findElement(By.tagName("a")).findElement(By.tagName("img"));
                    return new StreamDetails(
                            link.getText(),
                            link.getAttribute("href"),
                            isImageRetrievable() ? img.getAttribute("src") : "",
                            website.getId(),
                            website.getName());
                })
                .collect(Collectors.toList());
    }
}
