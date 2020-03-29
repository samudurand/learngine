package com.learngine.api.formatting;

import com.learngine.common.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToLanguageConverterTest {

    private StringToLanguageConverter converter;

    @BeforeEach
    void setUp() {
         converter = new StringToLanguageConverter();
    }

    @Test
    void convertStringToLanguage() {
        var result = converter.convert("en");
        assertEquals(Language.ENGLISH, result);
    }

    @Test
    void convertUknownStringToLanguageFails() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("unknown"));
    }

    @Test
    void convertEmptyStringToLanguageFails() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(""));
    }


}