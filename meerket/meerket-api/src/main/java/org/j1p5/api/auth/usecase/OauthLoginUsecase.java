package org.j1p5.api.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.service.OauthClientService;
import org.j1p5.api.auth.service.OauthService;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLoginUsecase {

    private final OauthClientService oauthClientService;
    private final OauthService oauthService;

    public UserInfo execute(String code, String provider) {
        Provider socialProvider = Provider.get(provider);
        OauthProfile profile = oauthClientService.request(code, socialProvider);

        return oauthService.login(profile, socialProvider);
    }
}
