package org.j1p5.api.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.api.auth.service.OauthSenderService;
import org.j1p5.api.auth.service.OauthService;
import org.j1p5.domain.activityArea.dto.SimpleAddress;
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

    /**
     * 소셜 로그인
     *
     * @author icecoff22
     * @param code
     * @param provider
     * @return 유저 정보, 활동 지역
     */
    public UserInfo login(String code, String provider) {
        OauthProfile profile =
                oauthSenderService.request(code, provider); // 서드 파티에 요청을 보내서 정보를 받아온다.
        UserEntity user = oauthService.login(profile, Provider.get(provider)); // 로그인을 시도한다.

        ActivityArea activityArea =
                activityAreaService.getActivityAreaByUser(user.getId()); // 유저에 해당하는 활동 지역을 조회한다.

        if (activityArea == null) {
            return UserInfo.of(user, null);
        }

        return UserInfo.of(user, SimpleAddress.from(activityArea)); // 리턴한다.
    }
}
