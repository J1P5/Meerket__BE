package org.j1p5.domain.auth;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static org.j1p5.domain.user.entity.Provider.KAKAO;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;

    public void login(String code, String provider) {

        OauthProfile oauthProfile = switch (Provider.valueOf(provider)) {
            case KAKAO -> ;
            case NAVER -> ;
        };

        if ()

    }
}
