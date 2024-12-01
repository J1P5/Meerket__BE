package org.j1p5.api.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String FRONT_ADDRESS = "http://localhost:3000";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONT_ADDRESS)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("location")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}
