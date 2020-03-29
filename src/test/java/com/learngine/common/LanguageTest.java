package com.learngine.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void findLanguageByShortName() {
        var result = Language.valueOfName("es");
        assertEquals(Language.SPANISH, result);
    }

    @Test
    void defaultsToUknownLanguage() {
        var result = Language.valueOfName("zz");
        assertEquals(Language.UNKNOWN, result);
    }

    @Test
    void defaultsToUknownLanguageForEmptyInput() {
        var result = Language.valueOfName("");
        assertEquals(Language.UNKNOWN, result);
    }

    @Test
    void defaultsToUknownLanguageForNullInput() {
        var result = Language.valueOfName(null);
        assertEquals(Language.UNKNOWN, result);
    }
}