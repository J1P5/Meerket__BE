package org.j1p5.api.auth;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.util.AuthorityUtil;
import org.j1p5.domain.user.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SecurityContext setContext(UserInfo user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user.pk(), null, AuthorityUtil.convertToAuthorities(user.role()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return SecurityContextHolder.getContext();
    }
}
