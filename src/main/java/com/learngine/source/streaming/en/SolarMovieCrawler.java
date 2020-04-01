package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamCompleteDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class SolarMovieCrawler extends HeadlessCrawler {

    public SolarMovieCrawler(SolarMovie website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return getOrCreateClient().getPage(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamCompleteDetails> parseResults(HtmlPage page) {
        List<HtmlElement> elts = page.getByXPath("//div[@class='result-item']");
        return elts.stream()
                .map(elt -> {
                    HtmlAnchor link = elt.getFirstByXPath(".//div[@class='title']/a");
                    HtmlImage img = elt.getFirstByXPath(".//img");
                    return new StreamCompleteDetails(
                            link.getTextContent(),
                            link.getHrefAttribute(),
                            img.getSrcAttribute(),
                            website.getId(),
                            website.getName()
                    );
                }).collect(Collectors.toList());
    }
}
