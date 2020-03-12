package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieMetadataSearchResult {
    private Integer page;
    private Integer total_results;

    @JsonProperty("results")
    private List<MovieMetadata> movies;
}
