package org.j1p5.api.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.api.auth.service.OauthSenderService;
import org.j1p5.api.auth.service.OauthService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.user.UserInfo;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OauthLoginUsecase {

    private final OauthSenderService oauthSenderService;
    private final OauthService oauthService;
    private final FcmService fcmService;
    private final ActivityAreaService activityAreaService;

    /**
     * 소셜 로그인
     * @author icecoff22
     * @param code
     * @param provider
     * @return 유저 정보, 활동 지역
     */
    public UserInfo login(String code, String provider, String fcmToken) {
        OauthProfile profile = oauthSenderService.request(code, provider);
        UserEntity user = oauthService.login(profile, Provider.get(provider));
        fcmService.saveFcmToken(user.getId(), fcmToken);

        ActivityArea activityArea = activityAreaService.getActivityAreaByUser(user.getId());

        if (activityArea == null) {
            return UserInfo.of(user, null);
        }

        return UserInfo.of(user, SimpleAddress.from(activityArea));
    }
}
