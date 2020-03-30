package com.learngine.source;

import com.learngine.source.streaming.StreamDetails;

import java.util.List;

public interface WebsiteCrawler {

    Website getWebsite();

    List<StreamDetails> searchTitleByName(String title);

    void closeClient();
}
