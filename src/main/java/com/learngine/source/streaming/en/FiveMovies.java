package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.common.Language;
import com.learngine.source.HtmlUnitBrowsable;
import com.learngine.source.HtmlUnitWebsiteHandler;
import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.encodeSearchParams;

public class FiveMovies implements Website, HtmlUnitBrowsable {
    @Override
    public String getName() {
        return "5 Movies";
    }

    @Override
    public String getUrl() {
        return "https://5movies.cloud";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }

    @Override
    public HtmlUnitWebsiteHandler getHandler() {
        return new Handler(this);
    }

    public static class Handler extends HtmlUnitWebsiteHandler {

        public Handler(Website website) {
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
                            website.getName()
                    )).collect(Collectors.toList());
        }
    }
}
