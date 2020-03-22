package com.learngine.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MovieSummary {
    private Integer id;
    private String title;
    private String imageUrl;

    @JsonFormat(pattern = "yyyy")
    private Date date;

    private String description;
}
