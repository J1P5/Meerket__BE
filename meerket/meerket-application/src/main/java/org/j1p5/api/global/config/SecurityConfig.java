package org.j1p5.api.global.config;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.global.handler.CustomAccessDeniedHandler;
import org.j1p5.api.global.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // TODO : csrf 추후 도입
                .cors(c -> c.configurationSource(corsConfig.configurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                authorize -> {
                    authorize.anyRequest().permitAll(); // init setting

                    //
                    // authorize.requestMatchers("/api/v1/admin").hasRole("ADMIN");
                });

        http.exceptionHandling(
                e -> {
                    e.accessDeniedHandler(customAccessDeniedHandler);
                    e.authenticationEntryPoint(customAuthenticationEntryPoint);
                });

        http.sessionManagement(
                session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false));

        return http.build();
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        List<SessionAuthenticationStrategy> sessionAuthenticationStrategies = List.of(
                concurrentSessionControlAuthenticationStrategy(),
                new RegisterSessionAuthenticationStrategy(sessionRegistry())
        );

        return new CompositeSessionAuthenticationStrategy(sessionAuthenticationStrategies);
    }

    @Bean
    public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy
                = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurrentSessionControlAuthenticationStrategy.setMaximumSessions(1);

        return concurrentSessionControlAuthenticationStrategy;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
