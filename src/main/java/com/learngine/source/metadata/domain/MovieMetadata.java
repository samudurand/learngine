package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieMetadata {
    Integer id;

    @JsonProperty(value = "original_title")
    String originalTitle;

    @JsonProperty("release_date")
    Optional<String> releaseDate;

    @JsonProperty("poster_path")
    Optional<String> posterPath;

    Optional<String> overview;

    @JsonProperty("vote_average")
    Float voteAverage;
}
