package com.learngine.api.domain;

import com.learngine.source.streaming.StreamDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class StreamsSearchResults {
    List<StreamDetails> streams;
    Set<String> alternativeTitles;
}
