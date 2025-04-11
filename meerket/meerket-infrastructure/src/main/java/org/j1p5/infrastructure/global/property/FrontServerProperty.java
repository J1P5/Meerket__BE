package org.j1p5.infrastructure.global.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("front.server")
public record FrontServerProperty(
    String baseUri
) {
}
