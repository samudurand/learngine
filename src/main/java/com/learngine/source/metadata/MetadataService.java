package com.learngine.source.metadata;

import com.learngine.common.Language;
import com.learngine.api.domain.MovieSummary;
import com.learngine.configuration.MetadataRetrievalFailedException;
import com.learngine.source.metadata.domain.AlternativeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MetadataService {

    private TheMovieDB metadataSource;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private String imageDimensions = "w92";
    private String baseImagePath = "http://image.tmdb.org/t/p/w92";

    @Autowired
    public MetadataService(TheMovieDB metadataSource) {
        this.metadataSource = metadataSource;
    }

    public List<MovieSummary> findMatchingMovies(String title) {
        return metadataSource.findMoviesByTitle(title).stream()
                .map(movie -> {
                    try {
                        return new MovieSummary(
                                movie.getId(),
                                movie.getOriginalTitle(),
                                buildImagePath(movie.getPosterPath()),
                                format.parse(movie.getReleaseDate())
                        );
                    } catch (ParseException e) {
                        throw new MetadataRetrievalFailedException();
                    }
                }).collect(Collectors.toList());
    }

    public Set<String> findLocalizedTitles(String submittedTitle, Integer movieId, Language audio) {
        return metadataSource.findAlternativeTitles(movieId).stream()
                .filter(title -> audio.getCountries().contains(title.getCountry()))
                .map(AlternativeTitle::getTitle)
                .collect(Collectors.toSet());
    }

    private String buildImagePath(Optional<String> posterPath) {
        return posterPath.map(path -> String.format("%s%s", baseImagePath, path)).orElse("");
    }
}
