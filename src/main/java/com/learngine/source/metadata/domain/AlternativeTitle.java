package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learngine.common.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlternativeTitle {
    @JsonProperty("iso_3166_1")
    Country country;

    String Title;
}