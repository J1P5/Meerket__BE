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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/**"))
                .cors(c -> c.configurationSource(corsConfig.configurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                authorize -> {
                    authorize.requestMatchers("/api/v1/oauth/**").permitAll();
                    authorize.anyRequest().authenticated(); // init setting

                    //
                    // authorize.requestMatchers("/api/v1/admin").hasRole("ADMIN");
                });

        http.exceptionHandling(
                exception -> {
                    exception.authenticationEntryPoint(customAuthenticationEntryPoint);
                    exception.accessDeniedHandler(customAccessDeniedHandler);
                }
        );

        http.sessionManagement(
                session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false));

        return http.build();
    }
}
