package com.learngine.source.metadata;

import com.learngine.api.domain.MovieSummary;
import com.learngine.common.Language;
import com.learngine.source.metadata.domain.AlternativeTitle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class MetadataService {

    private final Logger logger = LoggerFactory.getLogger(MetadataService.class);

    private TheMovieDB metadataSource;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String imageDimensions = "w92";
    private String baseImagePath = "http://image.tmdb.org/t/p/" + imageDimensions;

    @Autowired
    public MetadataService(TheMovieDB metadataSource) {
        this.metadataSource = metadataSource;
    }

    public Flux<MovieSummary> findMatchingMovies(String title) {
        return metadataSource.findMoviesByTitle(title)
                .map(movie -> new MovieSummary(
                        movie.getId(),
                        movie.getOriginalTitle(),
                        buildImagePath(movie.getPosterPath()),
                        formatDate(movie.getReleaseDate())
                ));
    }

    private Date formatDate(Optional<String> date) {
        return date.map(d -> {
            try {
                return format.parse(d);
            } catch (ParseException e) {
                logger.error("Parsing of date failed : " + d);
                return null;
            }
        }).orElse(null);
    }

    private String buildImagePath(Optional<String> posterPath) {
        return posterPath.map(path -> String.format("%s%s", baseImagePath, path)).orElse("");
    }

    public Flux<String> findLocalizedTitles(String submittedTitle, Integer movieId, Language audio) {
        return metadataSource.findAlternativeTitles(movieId)
                .filter(title -> audio.getCountries().contains(title.getCountry()))
                .map(AlternativeTitle::getTitle);
    }
}
