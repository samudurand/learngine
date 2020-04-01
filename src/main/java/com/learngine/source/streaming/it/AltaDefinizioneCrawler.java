package com.learngine.source.streaming.it;

import com.learngine.crawler.UICrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizioneCrawler extends UICrawler {

    public AltaDefinizioneCrawler(Supplier<WebDriver> browserSupplier) {
        super(new AltaDefinizione(), browserSupplier);
    }

    @Override
    protected void performSearch(String title) {
        getOrCreateBrowser().get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamCompleteDetails> parseResults() {
        return getOrCreateBrowser().findElements(By.xpath("//h5[@class='titleFilm']"))
                .stream()
                .map(elt -> {
                    var link = elt.findElement(By.xpath(".//parent::div/parent::div/parent::a"));
                    var img = elt.findElement(By.xpath(".//parent::div/parent::div//img"));
                    return new StreamCompleteDetails(
                            elt.getText(),
                            link.getAttribute("href"),
                            isImageRetrievable() ? img.getAttribute("src") : "",
                            website.getId(),
                            website.getName());
                })
                .collect(Collectors.toList());
    }
}
