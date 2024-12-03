package org.j1p5.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLoginUsecase {

    private final OauthSenderService oauthSenderService;
    private final OauthService oauthService;

    public UserInfo login(String code, String provider) {
        OauthProfile profile = oauthSenderService.request(code, provider);

        return oauthService.login(profile, Provider.valueOf(provider));
    }
}
