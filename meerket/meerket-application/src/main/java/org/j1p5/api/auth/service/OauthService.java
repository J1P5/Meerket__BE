package org.j1p5.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.Role;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;

    public UserEntity login(OauthProfile profile, Provider provider) {
        UserEntity user = userRepository.findBySocialEmailAndProvider(profile.getEmail(), provider)
                        .orElse(null);

        if (user == null) {
            return userRepository.save(
                    UserEntity.create(profile.getId(), profile.getEmail(), provider, Role.USER)
            );
        }
        return user;
    }
}
