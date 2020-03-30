package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.source.UrlFormatter;
import com.learngine.source.Website;
import com.learngine.source.htmlunit.HtmlUnitWebsiteCrawler;
import com.learngine.source.streaming.StreamDetails;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.alternativeEncodeSearchParams;

class ISubsMoviesCrawler extends HtmlUnitWebsiteCrawler {

    public ISubsMoviesCrawler(Website website) {
        super(website);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return client.getPage(String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title)));
    }

    @Override
    protected List<StreamDetails> parseResults(HtmlPage page) {
        List<HtmlElement> elts = page.getByXPath("//figcaption");
        return elts.stream()
                .map(elt -> new StreamDetails(
                        ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent(),
                        UrlFormatter.generateFullLink(website.getUrl(),
                                ((HtmlAnchor) elt.getFirstByXPath(".//parent::figure/parent::a")).getHrefAttribute()),
                        UrlFormatter.generateFullLink(website.getUrl(),
                                ((HtmlImage) elt.getFirstByXPath(".//parent::figure//img")).getSrcAttribute()),
                        website.getId(),
                        website.getName()
                )).collect(Collectors.toList());
    }
}
