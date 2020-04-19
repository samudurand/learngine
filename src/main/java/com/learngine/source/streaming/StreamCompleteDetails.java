package com.learngine.source.streaming;

import com.learngine.source.Website;
import com.learngine.source.utils.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.learngine.source.utils.HttpUtils.sanitizeHTML;
import static com.learngine.source.utils.StringUtils.removeExtraWhitespaces;

@Data
@NoArgsConstructor
public class StreamCompleteDetails {
    private String title;
    private String link;
    private String imageUrl;
    private String sourceId;
    private String source;
    private String alternativeUrl;

    public StreamCompleteDetails(String title, String link, String imageUrl, String sourceId, String source, String alternativeUrl) {
        this.title = sanitizeHTML(title);
        this.link = sanitizeHTML(link);
        this.imageUrl = sanitizeHTML(imageUrl);
        this.sourceId = sourceId;
        this.source = source;
        this.alternativeUrl = alternativeUrl;
    }

    public StreamCompleteDetails(StreamHtmlParsedData parsedData, Website source) {
        this.title = removeExtraWhitespaces(sanitizeHTML(parsedData.getTitle().toLowerCase()));
        this.link = sanitizeHTML(parsedData.getLink());
        this.imageUrl = sanitizeHTML(parsedData.getImageUrl());
        this.sourceId = source.getId();
        this.source = source.getName();
        this.alternativeUrl = source.getAlternativeUrl().orElse("");
    }
}
