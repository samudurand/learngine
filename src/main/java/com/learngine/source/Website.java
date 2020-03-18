package com.learngine.source;

import com.learngine.common.Language;

import java.util.Optional;

public interface Website {
    String getName();

    String getUrl();

    /**
     * Indicates where the latest URL can be found. Some websites change URL regularly.
     */
    default Optional<String> getReferenceUrl() { return Optional.empty(); }

    Language getAudioLanguage();

    /**
     * Indicates if the search is performed via the UI instead of a direct HTTP call.
     */
    default Boolean isUiBasedSearch() {
        return false;
    }

    default Boolean isCloudflareProtected() {
        return false;
    }
}
