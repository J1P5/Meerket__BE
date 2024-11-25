package org.j1p5.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public UserEntity read(String email, String provider) {
        return userRepository.findBySocialEmailAndProvider(email, Provider.valueOf(provider))
                .orElse(null);
    }
}
