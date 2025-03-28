package org.j1p5.api.user.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWithdrawService {

    private final UserRepository userRepository;

    // 유저 탈퇴 시 유저 도메인 관련 삭제 처리
    @Transactional
    public void withdraw(UserEntity user) {
        user.withdraw();
        userRepository.save(user);
    }
}
