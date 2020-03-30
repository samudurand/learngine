package com.learngine.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class HeadlessCrawlerConfig {

    @Bean
    public Supplier<WebClient> defaultWebClient() {
        return () -> {
            final WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            client.getOptions().setRedirectEnabled(true);
            client.getOptions().setUseInsecureSSL(true);
            client.getCache().setMaxSize(0);
            client.setJavaScriptTimeout(10000);
            return client;
        };
    }
}
