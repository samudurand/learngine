package com.learngine;

import com.learngine.common.Language;
import com.learngine.source.Website;

public class TestWebsite implements Website {

    private String id;
    private String name;
    private Language audio;

    public TestWebsite(String id, String name, Language audio) {
        this.id = id;
        this.name = name;
        this.audio = audio;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return "http://example.com";
    }

    @Override
    public Language getAudioLanguage() {
        return audio;
    }
}
