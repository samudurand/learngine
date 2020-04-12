package com.learngine.source.metadata;

import com.learngine.api.MovieSummary;
import com.learngine.common.Language;
import com.learngine.source.metadata.domain.AlternativeTitle;
import com.learngine.source.metadata.domain.MovieMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@Slf4j
public class MetadataService {

    /**
     * Date used when the release date cannot be determined. It is better to return some incorrect data than none.
     */
    public final static LocalDate DEFAULT_RELEASE_DATE = LocalDate.parse("2000-01-01");

    private final static String IMG_DIMENSIONS = "w92";
    private final static String BASE_IMG_PATH = "http://image.tmdb.org/t/p/" + IMG_DIMENSIONS;

    private final MetadataSource metadataSource;

    @Autowired
    public MetadataService(MetadataSource metadataSource) {
        this.metadataSource = metadataSource;
    }

    public Flux<MovieSummary> findMatchingMovies(String title) {
        return metadataSource.searchMoviesByTitle(title)
                .map(convertToSummary());
    }

    private Function<MovieMetadata, MovieSummary> convertToSummary() {
        return movie -> new MovieSummary(
                movie.getId(),
                buildTitle(movie),
                buildImagePath(movie.getPosterPath()),
                buildDate(movie.getReleaseDate()),
                buildDescription(movie.getOverview()),
                movie.getVoteAverage()
        );
    }

    private String buildTitle(MovieMetadata movie) {
        return movie.getOriginalTitle().toLowerCase();
    }

    private LocalDate buildDate(Optional<String> date) {
        return date
                .map(this::parseDate)
                .orElse(DEFAULT_RELEASE_DATE);
    }

    private LocalDate parseDate(String d) {
        try {
            return LocalDate.parse(d);
        } catch (DateTimeParseException e) {
            log.debug("Parsing of date failed : [" + d + "]", e);
            return DEFAULT_RELEASE_DATE;
        }
    }

    private String buildImagePath(Optional<String> posterPath) {
        return posterPath
                .map(path -> String.format("%s%s", BASE_IMG_PATH, path))
                .orElse("");
    }

    private String buildDescription(Optional<String> overview) {
        return overview.orElse("");
    }

    public Flux<String> findLocalizedTitles(Integer movieId, Language audio) {
        return metadataSource.findAlternativeTitles(movieId)
                .filter(isTitleFromCountrySpeakingWantedLanguage(audio))
                .map(AlternativeTitle::getTitle)
                .distinct();
    }

    private Predicate<AlternativeTitle> isTitleFromCountrySpeakingWantedLanguage(Language audio) {
        return title -> audio.getCountries().contains(title.getCountry());
    }
}
