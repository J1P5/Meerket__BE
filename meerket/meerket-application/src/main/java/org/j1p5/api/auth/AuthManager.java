package org.j1p5.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.dto.SessionInfo;
import org.j1p5.api.auth.util.AuthorityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public void setupAuthenticationContext(SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        sessionInfo.pk(), null, AuthorityUtil.convertToAuthorities(sessionInfo.role()));

        sessionAuthenticationStrategy.onAuthentication(authenticationToken, request, response);

        saveSecurityContext(authenticationToken, request, response);
    }

    private void saveSecurityContext(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);
    }
}
