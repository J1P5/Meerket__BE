package org.j1p5.domain.auth;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.service.UserAppender;
import org.j1p5.domain.user.service.UserReader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthSender oauthSender;
    private final UserReader userReader;
    private final UserAppender userAppender;

    public UserInfo login(String code, String provider) {
        OauthProfile profile = oauthSender.request(code, provider);

        UserEntity user = userReader.read(profile.getEmail(), provider);
        if (user == null) {
            user = userAppender.append(profile, provider);
            return UserInfo.from(user);
        }

        return UserInfo.from(user);
    }
}
