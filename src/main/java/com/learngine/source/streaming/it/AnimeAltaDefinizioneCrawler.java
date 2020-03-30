package com.learngine.source.streaming.it;

import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamDetails;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Slf4j
@Component
public class AnimeAltaDefinizioneCrawler extends UICrawler {

    public AnimeAltaDefinizioneCrawler(Supplier<WebDriver> browserSupplier) {
        super(new AnimeAltaDefinizione(), browserSupplier);
    }

    @Override
    protected void performSearch(String title) {
        getBrowser().get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults() {
        var result = getBrowser().findElements(By.className("article-image"));
        log.info("Found {} elements", result.size());
        return getBrowser().findElements(By.className("article-image"))
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
