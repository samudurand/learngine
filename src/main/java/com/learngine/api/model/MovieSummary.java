package com.learngine.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MovieSummary {
    private Integer id;
    private String title;
    private String imageUrl;

    @JsonFormat(pattern = "yyyy")
    private LocalDate date;

    private String description;
    private Float voteAverage;
}
