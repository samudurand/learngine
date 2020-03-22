package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class MovieMetadata {
    Integer id;

    @JsonProperty(value = "original_title")
    String originalTitle;

    @JsonProperty("release_date")
    Optional<String> releaseDate;

    @JsonProperty("poster_path")
    Optional<String> posterPath;

    Optional<String> overview;
}
