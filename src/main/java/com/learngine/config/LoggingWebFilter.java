package com.learngine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
@Slf4j
public class LoggingWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {

        final var request = serverWebExchange.getRequest();
        log.info("Path: {} \n , Response code: {}",
                request.getPath().toString(),
                serverWebExchange.getResponse().getRawStatusCode());

        return webFilterChain.filter(serverWebExchange);
    }
}