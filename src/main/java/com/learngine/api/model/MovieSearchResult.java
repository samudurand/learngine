package com.learngine.api.model;

import com.learngine.api.model.MovieSummary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieSearchResult {
    Integer totalPages;
    List<MovieSummary> movies;
}
