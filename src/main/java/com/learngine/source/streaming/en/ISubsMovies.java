package com.learngine.source.streaming.en;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.htmlunit.HtmlUnitBrowsable;
import com.learngine.source.htmlunit.HtmlUnitWebsiteHandler;
import com.learngine.source.streaming.StreamDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.learngine.source.HttpUtils.alternativeEncodeSearchParams;

@Component
public class ISubsMovies implements Website, HtmlUnitBrowsable {
    @Override
    public String getId() {
        return "isubsmovies";
    }

    @Override
    public String getName() {
        return "I Subs Movies";
    }

    @Override
    public String getUrl() {
        return "https://isubsmovies.com";
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
            return client.getPage(String.format("%s/search/%s", website.getUrl(), alternativeEncodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults(HtmlPage page) {
            List<HtmlElement> elts = page.getByXPath("//figcaption");
            return elts.stream()
                    .map(elt -> new StreamDetails(
                            ((HtmlHeading2) elt.getFirstByXPath(".//h2")).getTextContent(),
                            buildFullLink(((HtmlAnchor) elt.getFirstByXPath(".//parent::figure/parent::a")).getHrefAttribute()),
                            buildFullLink(((HtmlImage) elt.getFirstByXPath(".//parent::figure//img")).getSrcAttribute()),
                            website.getId(),
                            website.getName()
                    )).collect(Collectors.toList());
        }
    }
}
