package com.learngine.source;

import com.learngine.common.Language;
import org.springframework.stereotype.Component;

public interface Website {
    String getName();

    String getUrl();

    Language getAudioLanguage();

    /**
     * Indicates if the search is performed via the UI instead of a direct HTTP call.
     *
     * @return
     */
    default Boolean isUiBasedSearch() {
        return false;
    }

    default Boolean isCloudflareProtected() {
        return false;
    }
}
