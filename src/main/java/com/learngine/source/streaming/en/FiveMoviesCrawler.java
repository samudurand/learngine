package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.encodeSearchParams;

@Component
public class FiveMoviesCrawler extends HeadlessCrawler {

    public FiveMoviesCrawler(Supplier<WebClient> clientSupplier) {
        super(new FiveMovies(), clientSupplier);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return getClient().getPage(String.format("%s/movie/search/%s", website.getUrl(), encodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults(HtmlPage page) {
        List<HtmlElement> elts = page.getByXPath("//div[@class='ml-item']");
        return elts.stream()
                .map(elt -> new StreamDetails(
                        ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent(),
                        ((HtmlAnchor) elt.getFirstByXPath(".//a")).getHrefAttribute(),
                        ((HtmlImage) elt.getFirstByXPath(".//a//img")).getAttribute("data-original"),
                        website.getId(),
                        website.getName())
                ).collect(Collectors.toList());
    }
}
