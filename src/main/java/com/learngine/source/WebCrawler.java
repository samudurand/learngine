package com.learngine.source;

import com.learngine.api.domain.StreamsSearchResults;
import com.learngine.common.Language;
import com.learngine.source.metadata.MetadataService;
import com.learngine.source.searchengine.Unog;
import com.learngine.source.streaming.SearchEngine;
import com.learngine.source.streaming.StreamDetails;
import com.learngine.source.streaming.en.FiveMovies;
import com.learngine.source.streaming.en.ISubsMovies;
import com.learngine.source.streaming.en.SolarMovie;
import com.learngine.source.streaming.fr.FilmFra;
import com.learngine.source.streaming.fr.StreamComplet;
import com.learngine.source.streaming.it.AltaDefinizione;
import com.learngine.source.streaming.it.AnimeAltaDefinizione;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class WebCrawler {

    private final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final MetadataService metadataSource;

    private final List<Website> sources = List.of(
            // Search Engines
            new Unog(),
            // French
            new FilmFra(),
            new StreamComplet(),
            // English
            new FiveMovies(),
            new ISubsMovies(),
            new SolarMovie(),
            // Italian
            new AltaDefinizione(),
            new AnimeAltaDefinizione()
    );

    @Autowired
    public WebCrawler(final MetadataService metadataSource) {
        this.metadataSource = metadataSource;
    }

    public StreamsSearchResults search(String movieTitle, Integer movieId, Language audio, Language subtitles) {
        final var alternativeTitles = metadataSource.findLocalizedTitles(movieTitle, movieId, audio);
        final var compatibleSources = findCompatibleSources(audio);

        var streams = compatibleSources.parallelStream()
                .map(website -> {
                    final var browser = configuredBrowser();
                    final var websiteHandler = website.getHandler(browser);
                    try {

                        if (website.isCloudflareProtected() || website.isUiBasedSearch()) {
                            websiteHandler.navigateToWebsite();
                        }

                        final var results = websiteHandler.searchTitleByName(movieTitle);
                        browser.quit();
                        return results;
                    } catch (Exception e) {
                        logger.error("An exception occurred during the search on website " + website.getName(), e);
                        browser.quit();
                        return new ArrayList<StreamDetails>();
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new StreamsSearchResults(streams, alternativeTitles);
    }

    private List<Website> findCompatibleSources(Language audio) {
        return sources.stream()
                .filter(website -> website instanceof SearchEngine || website.getAudioLanguage().equals(audio))
                .collect(Collectors.toList());
    }

//    private List<StreamDetails> findMatchingTitles(String searchedName, List<StreamDetails> titlesFound) {
//        return titlesFound.stream()
//                .filter(details -> details.getTitle().toLowerCase().trim().contains(searchedName))
//                .collect(Collectors.toList());
//    }

    private WebDriver configuredBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        var browser = new ChromeDriver(options);
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return browser;
    }
}
