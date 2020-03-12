package com.learngine.source;

import com.learngine.common.Language;
import org.openqa.selenium.WebDriver;

public interface Website {
    String getName();
    String getUrl();
    Language getAudioLanguage();

    /**
     * Indicates if the search is performed via the UI instead of a direct HTTP call.
     * @return
     */
    default Boolean isUiBasedSearch() {
        return false;
    }
    default Boolean isCloudflareProtected() {
        return false;
    }

    WebsiteHandler getHandler(WebDriver browser);
}
