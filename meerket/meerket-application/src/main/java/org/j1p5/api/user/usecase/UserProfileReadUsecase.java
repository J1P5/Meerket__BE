package org.j1p5.api.user.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.api.user.service.UserProfileService;
import org.j1p5.domain.activityArea.dto.SimpleAddress;
import org.j1p5.domain.user.UserProfile;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileReadUsecase {

    private final UserProfileService userProfileService;
    private final ActivityAreaService activityAreaService;

    public UserProfile execute(Long userId) {
        UserEntity user = userProfileService.getUserById(userId);
        SimpleAddress emdAddress = activityAreaService.getActivityEmdAreaByUserId(userId);

        return UserProfile.of(user, emdAddress);
    }
}
