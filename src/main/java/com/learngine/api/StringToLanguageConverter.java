package com.learngine.api;

import com.learngine.common.Language;
import org.springframework.core.convert.converter.Converter;

public class StringToLanguageConverter implements Converter<String, Language> {
    @Override
    public Language convert(String language) {
        final var result = Language.valueOfName(language.toLowerCase());
        if (Language.UNSUPPORTED.equals(result)) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
