package com.learngine.common;

import java.util.List;

import static com.learngine.common.Country.ES;
import static com.learngine.common.Country.FR;
import static com.learngine.common.Country.GB;
import static com.learngine.common.Country.IT;
import static com.learngine.common.Country.US;

//TODO consider using Locale.getIsoLanguage() or even http://site.icu-project.org/ for complete support
// https://www.tutorialspoint.com/java/util/locale_getisolanguages.htm
// TODO add more countries corresponding to those languages
public enum Language {
    ENGLISH("en", List.of(GB, US)),
    FRENCH("fr", List.of(FR)),
    SPANISH("es", List.of(ES)),
    ITALIAN("it", List.of(IT)),
    UNKNOWN("unknown", List.of());

    private final String shortName;
    private final List<Country> countries;

    Language(String shortName, List<Country> countries) {
        this.shortName = shortName;
        this.countries = countries;
    }

    public static Language valueOfName(String shortName) {
        for (Language lang : values()) {
            if (lang.shortName.equals(shortName)) {
                return lang;
            }
        }
        return UNKNOWN;
    }

    public String getShortName() {
        return shortName;
    }

    public List<Country> getCountries() {
        return countries;
    }
}
