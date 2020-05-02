package com.learngine.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieSearchResult {
    Integer totalPages;
    List<MovieSummary> movies;
}
