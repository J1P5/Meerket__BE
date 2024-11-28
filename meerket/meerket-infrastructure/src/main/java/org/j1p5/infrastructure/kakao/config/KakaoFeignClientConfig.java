package org.j1p5.infrastructure.kakao.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.j1p5.infrastructure.global.config.FeignErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.j1p5.infrastructure.kakao")
public class KakaoFeignClientConfig {

    @Bean
    public Logger.Level kakaoFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder kakaoFeignErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
