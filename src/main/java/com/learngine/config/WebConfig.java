package com.learngine.config;

import com.learngine.api.StringToLanguageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Value("${learngineui.domain}")
    private String uiDomain;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLanguageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(uiDomain);
    }
}
