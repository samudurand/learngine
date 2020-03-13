package com.learngine.source.metadata.domain;

import lombok.Data;

import java.util.List;

@Data
public class AlternativeTitleSearchResult {
    Integer id;
    List<AlternativeTitle> titles;
}