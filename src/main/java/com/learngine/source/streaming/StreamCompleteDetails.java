package com.learngine.source.streaming;

import com.learngine.source.Website;
import lombok.Data;

@Data
public class StreamCompleteDetails {
    private String title;
    private String link;
    private String imageUrl;
    private String sourceId;
    private String source;

    public StreamCompleteDetails(String originaTitle, String link, String imageUrl, String sourceId, String source) {
        this.title = originaTitle;
        this.link = link;
        this.imageUrl = imageUrl;
        this.sourceId = sourceId;
        this.source = source;
    }

    public StreamCompleteDetails(StreamHtmlParsedData parsedData, Website source) {
        this.title = parsedData.getTitle().toLowerCase();
        this.link = parsedData.getLink();
        this.imageUrl = parsedData.getImageUrl();
        this.sourceId = source.getId();
        this.source = source.getName();
    }
}
