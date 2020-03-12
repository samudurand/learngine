package com.learngine.source.metadata.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learngine.common.Country;
import lombok.Data;

import java.util.List;

@Data
public class AlternativeTitleSearchResult {
    Integer id;
    List<AlternativeTitle> titles;
}