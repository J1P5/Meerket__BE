package org.j1p5.api.user.usecase;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.service.UserProfileService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileSettingUsecase {

    private final UserProfileService userProfileService;

    /**
     * 프로필 수정
     *
     * @param userId
     * @param nickname
     * @param file
     */
    public void execute(Long userId, String nickname, File file) {
        if (file != null) {
            userProfileService.updateProfile(userId, file);
        }
        userProfileService.updateNickname(userId, nickname);
    }
}
