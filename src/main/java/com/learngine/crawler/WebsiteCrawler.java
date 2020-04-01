package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamCompleteDetails;

import java.util.List;

public interface WebsiteCrawler {

    Website getWebsite();

    List<StreamCompleteDetails> searchTitleByName(String title);

    void closeClient();
}
