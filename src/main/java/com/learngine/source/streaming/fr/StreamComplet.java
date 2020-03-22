package com.learngine.source.streaming.fr;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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

import static com.learngine.source.HttpUtils.encodeSearchParams;

@Component
public class StreamComplet implements Website, HtmlUnitBrowsable {
    @Override
    public String getId() {
        return "streamcomplet";
    }

    @Override
    public String getName() {
        return "Stream Complet";
    }

    @Override
    public String getUrl() {
        return "https://www.streamcomplet.page";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.FRENCH;
    }

    @Override
    public HtmlUnitWebsiteHandler getHandler() {
        return new Handler(this);
    }

    private static class Handler extends HtmlUnitWebsiteHandler {

        public Handler(Website website) {
            super(website);
        }

        @Override
        protected HtmlPage performSearch(String title) throws IOException {
            return client.getPage(String.format("%s/fr/search/?s=%s", website.getUrl(), encodeSearchParams(title)));
        }

        @Override
        protected List<StreamDetails> parseResults(HtmlPage page) {
            List<HtmlElement> elts = page.getByXPath("//div[@class='streamcompletpage_moviefilm']");
            return elts.stream()
                    .map(elt -> {
                        HtmlAnchor link = elt.getFirstByXPath(".//div[@class='streamcompletpage_movief']/a");
                        return new StreamDetails(
                                link.getTextContent(),
                                buildFullLink(link.getHrefAttribute()),
                                website.getId(),
                                website.getName()
                        );
                    }).collect(Collectors.toList());
        }
    }
}
