package com.learngine.crawler;

import com.learngine.source.Website;
import com.learngine.source.streaming.StreamDetails;

import java.util.List;

public interface WebsiteCrawler {

    Website getWebsite();

    List<StreamDetails> searchTitleByName(String title);

    void closeClient();
}
