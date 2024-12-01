package org.j1p5.infrastructure.naver.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.j1p5.infrastructure.global.config.FeignErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.j1p5.infrastructure.naver")
public class NaverFeignClientConfig {

    @Bean
    public Logger.Level naverFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder naverFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
