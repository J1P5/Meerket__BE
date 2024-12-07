package org.j1p5.api.global.config;

import java.util.List;
import org.j1p5.api.global.annotation.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CursorArgumentResolver cursorDefaultArgumentResolver;
    private OctetStreamMsgConverter octetStreamReadMsgConverter;

    public WebConfig(CursorArgumentResolver resolver, OctetStreamMsgConverter octetStreamReadMsgConverter) {
        this.cursorDefaultArgumentResolver = resolver;
        this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CursorArgumentResolver());
        resolvers.add(new LoginUserArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(octetStreamReadMsgConverter);
    }
}
