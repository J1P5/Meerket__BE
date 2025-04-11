package org.j1p5.infrastructure.global.config;

import org.j1p5.infrastructure.global.property.FrontServerProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FrontServerProperty.class)
public class PropertyConfig {
}
