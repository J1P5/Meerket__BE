package org.j1p5.api.global.config;

import org.j1p5.api.global.annotation.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CursorArgumentResolver cursorDefaultArgumentResolver;

    public WebConfig(CursorArgumentResolver resolver) {
        this.cursorDefaultArgumentResolver = resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CursorArgumentResolver());
        argumentResolvers.add(new LoginUserArgumentResolver());
    }
}