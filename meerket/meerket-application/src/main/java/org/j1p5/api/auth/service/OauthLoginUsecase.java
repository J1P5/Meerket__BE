package org.j1p5.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLoginUsecase {

    private final OauthSenderService oauthSenderService;
    private final OauthService oauthService;
    private final ActivityAreaService activityAreaService;

    public UserInfo login(String code, String provider) {
        OauthProfile profile = oauthSenderService.request(code, provider);
        UserEntity user = oauthService.login(profile, Provider.get(provider));

        ActivityArea activityArea = activityAreaService.getActivityAreaByUser(user.getId());

        return UserInfo.of(user, activityArea.getId());
    }
}
