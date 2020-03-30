package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.source.Website;
import com.learngine.source.htmlunit.HtmlUnitWebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class FiveMoviesCrawler extends HtmlUnitWebsiteCrawler {

    public FiveMoviesCrawler(Website website) {
        super(website);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return client.getPage(String.format("%s/movie/search/%s", website.getUrl(), encodeSearchParams(title)));
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
