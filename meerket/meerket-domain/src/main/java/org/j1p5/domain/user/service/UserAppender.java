package org.j1p5.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.OauthProfile;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.Role;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {

    private final UserRepository userRepository;

    public UserEntity append(OauthProfile profile, String provider) {
        return userRepository.save(UserEntity.create(
                        profile.getId(),
                        profile.getEmail(),
                        Provider.valueOf(provider),
                        Role.USER
        ));
    }
}
