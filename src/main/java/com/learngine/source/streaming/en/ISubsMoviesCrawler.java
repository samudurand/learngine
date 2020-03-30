package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.source.utils.UrlFormatter;
import com.learngine.crawler.HeadlessCrawler;
import com.learngine.source.streaming.StreamDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.learngine.source.utils.HttpUtils.alternativeEncodeSearchParams;

@Component
class ISubsMoviesCrawler extends HeadlessCrawler {

    public ISubsMoviesCrawler(Supplier<WebClient> clientSupplier) {
        super(new ISubsMovies(), clientSupplier);
    }

    @Override
    protected HtmlPage performSearch(String title) throws IOException {
        return getClient().getPage(String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title)));
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
