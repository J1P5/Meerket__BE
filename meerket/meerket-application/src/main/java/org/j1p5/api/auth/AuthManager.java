package org.j1p5.api.auth;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.dto.SessionInfo;
import org.j1p5.api.auth.util.AuthorityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SecurityContext setContext(SessionInfo sessionInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        sessionInfo.pk(),
                        null,
                        AuthorityUtil.convertToAuthorities(sessionInfo.role()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return SecurityContextHolder.getContext();
    }
}
