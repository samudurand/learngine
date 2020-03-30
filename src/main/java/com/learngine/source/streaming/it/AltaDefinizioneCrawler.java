package com.learngine.source.streaming.it;

import com.learngine.source.selenium.SeleniumWebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class AltaDefinizioneCrawler extends SeleniumWebsiteCrawler {

    public AltaDefinizioneCrawler(AltaDefinizione website, Supplier<WebDriver> browserSupplier) {
        super(website, browserSupplier);
    }

    @Override
    protected void performSearch(String title) {
        getBrowser().get(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults() {
        return getBrowser().findElements(By.xpath("//h5[@class='titleFilm']"))
                .stream()
                .map(elt -> {
                    var link = elt.findElement(By.xpath(".//parent::div/parent::div/parent::a"));
                    var img = elt.findElement(By.xpath(".//parent::div/parent::div//img"));
                    return new StreamDetails(
                            elt.getText(),
                            link.getAttribute("href"),
                            isImageRetrievable() ? img.getAttribute("src") : "",
                            website.getId(),
                            website.getName());
                })
                .collect(Collectors.toList());
    }
}
