package com.learngine.api.formatting;

import com.learngine.common.Language;
import org.springframework.core.convert.converter.Converter;

public class StringToLanguageConverter implements Converter<String, Language> {
    @Override
    public Language convert(String language) {
        final var result = Language.valueOfName(language.toLowerCase());
        if (Language.UNKNOWN.equals(result)) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
