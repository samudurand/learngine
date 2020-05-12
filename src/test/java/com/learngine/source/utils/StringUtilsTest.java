package com.learngine.source.utils;

import org.junit.jupiter.api.Test;

import static com.learngine.source.utils.StringUtils.removeExtraWhitespaces;
import static com.learngine.source.utils.StringUtils.truncate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void canProcessEmptyString() {
        assertEquals("", removeExtraWhitespaces(""));
    }

    @Test
    void removeAllTrailingSpaces() {
        assertEquals("hi", removeExtraWhitespaces(" hi "));
    }

    @Test
    void removeAllExtraInternalSpaces() {
        assertEquals("hi my name is max", removeExtraWhitespaces("hi     my  name is   max"));
    }

    @Test
    void removeAllNextLinesChars() {
        assertEquals("hi my name is max", removeExtraWhitespaces("hi my name \n is \n max"));
    }

    @Test
    void removeAllTabulationChars() {
        assertEquals("hi my name is max", removeExtraWhitespaces("hi my name \t is \t max"));
    }

    @Test
    void removeAllWhitespacesExceptWordSeparation() {
        assertEquals("hi my name is max", removeExtraWhitespaces(" hi\t\tmy name\n  is max   "));
    }

    @Test
    void truncateString() {
        assertEquals("abc", truncate("abcd", 3));
    }

    @Test
    void truncateShortString() {
        assertEquals("ab", truncate("ab", 3));
    }
}