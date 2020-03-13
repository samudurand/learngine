package com.learngine.source;

import com.gargoylesoftware.htmlunit.WebClient;
import com.learngine.source.streaming.StreamDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class WebsiteHandler {

    protected Website website;
    private Logger logger = LoggerFactory.getLogger(WebsiteHandler.class);

    public WebsiteHandler(Website website) {
        this.website = website;
    }

    protected void closeClient() {
        throw new NotImplementedException("Method not yet implemented");
    }
}
