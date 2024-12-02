package org.j1p5.api.user.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.service.UserRegisterService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNameRegisterUsecase {

    private final UserRegisterService userRegisterService;

    public void execute(Long userId, String nickname) {
        userRegisterService.updateNickname(userId, nickname);
    }
}