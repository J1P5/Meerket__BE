package org.j1p5.api.block.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.validator.BlockValidator;
import org.j1p5.domain.block.repository.BlockRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockDeleteService {

    private final BlockRepository blockRepository;
    private final BlockValidator blockValidator;

    @Transactional
    public void delete(Long userId, Long unblockId) {
        UserEntity user = blockValidator.validateUser(userId);
        UserEntity blockedUser = blockValidator.validateUser(unblockId);

        blockRepository.deleteByUserAndBlockedUser(user, blockedUser);
    }
}
