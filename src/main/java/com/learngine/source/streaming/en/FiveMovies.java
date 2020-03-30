package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.htmlunit.HtmlUnitBrowsable;
import com.learngine.source.htmlunit.HtmlUnitWebsiteCrawler;
import org.springframework.stereotype.Component;

@Component
public class FiveMovies implements Website, HtmlUnitBrowsable {

    private FiveMoviesCrawler crawler;

    @Override
    public String getId() {
        return "5movies";
    }

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
    public HtmlUnitWebsiteCrawler getHandler() {
        return new FiveMoviesCrawler(this);
    }
}
