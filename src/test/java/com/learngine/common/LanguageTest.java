package com.learngine.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageTest {

    @Test
    void findLanguageByShortName() {
        var result = Language.valueOfName("es");
        assertEquals(Language.SPANISH, result);
    }

    @Test
    void defaultsToUnsupportedLanguage() {
        var result = Language.valueOfName("zz");
        assertEquals(Language.UNSUPPORTED, result);
    }

    @Test
    void defaultsToUnsupportedLanguageForEmptyInput() {
        var result = Language.valueOfName("");
        assertEquals(Language.UNSUPPORTED, result);
    }

    @Test
    void defaultsToUnsupportedLanguageForNullInput() {
        var result = Language.valueOfName(null);
        assertEquals(Language.UNSUPPORTED, result);
    }
}