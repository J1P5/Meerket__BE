package org.j1p5.infrastructure.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.j1p5.infrastructure")
public class FeignClientConfig {
}
