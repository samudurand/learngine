package com.learngine.source.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void canProcessEmptyString() {
        assertEquals("", StringUtils.removeExtraWhitespaces(""));
    }

    @Test
    void removeAllTrailingSpaces() {
        assertEquals("hi", StringUtils.removeExtraWhitespaces(" hi "));
    }

    @Test
    void removeAllExtraInternalSpaces() {
        assertEquals("hi my name is max", StringUtils.removeExtraWhitespaces("hi     my  name is   max"));
    }

    @Test
    void removeAllNextLinesChars() {
        assertEquals("hi my name is max", StringUtils.removeExtraWhitespaces("hi my name \n is \n max"));
    }

    @Test
    void removeAllTabulationChars() {
        assertEquals("hi my name is max", StringUtils.removeExtraWhitespaces("hi my name \t is \t max"));
    }

    @Test
    void removeAllWhitespacesExceptWordSeparation() {
        assertEquals("hi my name is max", StringUtils.removeExtraWhitespaces(" hi\t\tmy name\n  is max   "));
    }
}