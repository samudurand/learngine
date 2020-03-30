package com.learngine.source.streaming.en;

import com.learngine.common.Language;
import com.learngine.source.Website;
import com.learngine.source.htmlunit.HtmlUnitWebsiteCrawler;
import org.springframework.stereotype.Component;

@Component
public class SolarMovie implements Website {
    @Override
    public String getId() {
        return "solarmovie";
    }

    @Override
    public String getName() {
        return "Solar Movie";
    }

    @Override
    public String getUrl() {
        return "https://solarmovie.network";
    }

    @Override
    public Language getAudioLanguage() {
        return Language.ENGLISH;
    }
}
