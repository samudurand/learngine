package com.learngine.source.streaming;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class StreamDetails {
    private String title;
    private String link;
    private String imageUrl;
    private String sourceId;
    private String source;

    public StreamDetails(String title, String link, String imageUrl, String sourceId, String source) {
        this.title = title.toLowerCase();
        this.link = link;
        this.imageUrl = imageUrl;
        this.sourceId = sourceId;
        this.source = source;
    }
}
