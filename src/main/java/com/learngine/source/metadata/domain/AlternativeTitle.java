package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learngine.common.Country;
import lombok.Data;

@Data
public class AlternativeTitle {
    @JsonProperty("iso_3166_1")
    Country country;

    String Title;
}