package org.j1p5.api.auth;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.j1p5.domain.user.service.UserReader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserReader userReader;

    @Override
    public UserDetails loadUserByUsername(String pk) throws UsernameNotFoundException {
        UserEntity user = userReader.getById(Long.parseLong(pk));
        return createUser(user);
    }

    private CustomUserDetail createUser(UserEntity user) {
        return CustomUserDetail.create(
                user.getId(),
                String.valueOf(user.getRole())
        );
    }
}
