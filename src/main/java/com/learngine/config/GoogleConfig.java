package com.learngine.config;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {

    @Bean
    public Translate getGoogleTranslateClient() {
        return TranslateOptions.getDefaultInstance().getService();
    }
}
