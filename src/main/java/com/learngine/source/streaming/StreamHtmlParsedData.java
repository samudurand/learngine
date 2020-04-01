package com.learngine.source.streaming;

import lombok.Data;

@Data
public class StreamHtmlParsedData {
    private String title;
    private String link;
    private String imageUrl;

    public StreamHtmlParsedData(String title, String link, String imageUrl) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
    }
}
