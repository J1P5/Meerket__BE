package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.block.entity.BlockEntity;
import org.j1p5.domain.block.repository.BlockRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BlockRegisterService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public void register(Long userId, Long blockUserId) {
        UserEntity blockedUser = validateUserId(blockUserId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        blockRepository.save(BlockEntity.create(blockedUser, user));
    }

    private UserEntity validateUserId(Long blockedUserId) {
        return userRepository.findById(blockedUserId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
}
