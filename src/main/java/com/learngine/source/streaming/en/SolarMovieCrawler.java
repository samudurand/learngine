package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.source.htmlunit.HtmlUnitWebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class SolarMovieCrawler extends HtmlUnitWebsiteCrawler {

    public SolarMovieCrawler(SolarMovie website, Supplier<WebClient> clientSupplier) {
        super(website, clientSupplier);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return getClient().getPage(String.format("%s?s=%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults(HtmlPage page) {
        List<HtmlElement> elts = page.getByXPath("//div[@class='result-item']");
        return elts.stream()
                .map(elt -> {
                    HtmlAnchor link = elt.getFirstByXPath(".//div[@class='title']/a");
                    HtmlImage img = elt.getFirstByXPath(".//img");
                    return new StreamDetails(
                            link.getTextContent(),
                            link.getHrefAttribute(),
                            img.getSrcAttribute(),
                            website.getId(),
                            website.getName()
                    );
                }).collect(Collectors.toList());
    }
}
